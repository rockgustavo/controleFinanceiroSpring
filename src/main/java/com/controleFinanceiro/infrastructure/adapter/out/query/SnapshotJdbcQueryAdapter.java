package com.controleFinanceiro.infrastructure.adapter.out.query;

import com.controleFinanceiro.application.dto.response.PosicaoResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SnapshotJdbcQueryAdapter implements SnapshotQueryPort {

    private final JdbcTemplate jdbc;

    private static final String FIND_ALL_RESUMO = """
            SELECT s.id, s.data, s.criado_em,
                   COUNT(p.id) AS numero_posicoes,
                   COALESCE(SUM(p.total_liq), 0) AS total_liq
            FROM snapshots s
            LEFT JOIN posicoes p ON p.snapshot_id = s.id
            GROUP BY s.id, s.data, s.criado_em
            ORDER BY s.data DESC
            """;

    private static final String FIND_BY_ID_FULL = """
            SELECT s.id AS snapshot_id, s.data, s.observacoes, s.criado_em,
                   p.ativo_id, a.nome AS nome_ativo, a.tipo AS tipo_ativo, a.ticker,
                   p.quantidade, p.preco_unit, p.total_bruto, p.total_liq, p.taxa
            FROM snapshots s
            LEFT JOIN posicoes p ON p.snapshot_id = s.id
            LEFT JOIN ativos a ON a.id = p.ativo_id
            WHERE s.id = ?
            ORDER BY a.nome
            """;

    private static final String FIND_LATEST_FULL = """
            SELECT s.id AS snapshot_id, s.data, s.observacoes, s.criado_em,
                   p.ativo_id, a.nome AS nome_ativo, a.tipo AS tipo_ativo, a.ticker,
                   p.quantidade, p.preco_unit, p.total_bruto, p.total_liq, p.taxa
            FROM snapshots s
            LEFT JOIN posicoes p ON p.snapshot_id = s.id
            LEFT JOIN ativos a ON a.id = p.ativo_id
            WHERE s.data = (SELECT MAX(data) FROM snapshots)
            ORDER BY a.nome
            """;

    @Override
    public List<SnapshotResumoResponse> findAll() {
        return jdbc.query(FIND_ALL_RESUMO, this::mapResumoRow);
    }

    @Override
    public Optional<SnapshotResponse> findById(UUID id) {
        var rows = jdbc.query(FIND_BY_ID_FULL, this::mapFullRow, id);
        return montarSnapshot(rows);
    }

    @Override
    public Optional<SnapshotResponse> findLatest() {
        var rows = jdbc.query(FIND_LATEST_FULL, this::mapFullRow);
        return montarSnapshot(rows);
    }

    private SnapshotResumoResponse mapResumoRow(ResultSet rs, int rowNum) throws SQLException {
        var criadoEm = rs.getObject("criado_em", OffsetDateTime.class);
        return new SnapshotResumoResponse(
                UUID.fromString(rs.getString("id")),
                rs.getObject("data", LocalDate.class),
                rs.getBigDecimal("total_liq"),
                rs.getInt("numero_posicoes"),
                criadoEm != null ? criadoEm.toInstant() : null
        );
    }

    private record FullRow(UUID snapshotId, LocalDate data, String observacoes, Instant criadoEm,
                           UUID ativoId, String nomeAtivo, String tipoAtivo, String ticker,
                           BigDecimal quantidade, BigDecimal precoUnit,
                           BigDecimal totalBruto, BigDecimal totalLiq, String taxa) {}

    private FullRow mapFullRow(ResultSet rs, int rowNum) throws SQLException {
        var ativoIdStr = rs.getString("ativo_id");
        var criadoEm = rs.getObject("criado_em", OffsetDateTime.class);
        return new FullRow(
                UUID.fromString(rs.getString("snapshot_id")),
                rs.getObject("data", LocalDate.class),
                rs.getString("observacoes"),
                criadoEm != null ? criadoEm.toInstant() : null,
                ativoIdStr != null ? UUID.fromString(ativoIdStr) : null,
                rs.getString("nome_ativo"),
                rs.getString("tipo_ativo"),
                rs.getString("ticker"),
                rs.getBigDecimal("quantidade"),
                rs.getBigDecimal("preco_unit"),
                rs.getBigDecimal("total_bruto"),
                rs.getBigDecimal("total_liq"),
                rs.getString("taxa")
        );
    }

    private Optional<SnapshotResponse> montarSnapshot(List<FullRow> rows) {
        if (rows.isEmpty()) return Optional.empty();

        var primeira = rows.get(0);
        var posicoes = rows.stream()
                .filter(r -> r.ativoId() != null)
                .map(r -> new PosicaoResponse(
                        r.ativoId(), r.nomeAtivo(), r.tipoAtivo(), r.ticker(),
                        r.quantidade(), r.precoUnit(), r.totalBruto(), r.totalLiq(), r.taxa()))
                .toList();

        BigDecimal totalBruto = posicoes.stream()
                .map(PosicaoResponse::totalBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLiq = posicoes.stream()
                .map(PosicaoResponse::totalLiq)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Optional.of(new SnapshotResponse(
                primeira.snapshotId(), primeira.data(), primeira.observacoes(),
                totalBruto, totalLiq, posicoes, primeira.criadoEm()));
    }
}
