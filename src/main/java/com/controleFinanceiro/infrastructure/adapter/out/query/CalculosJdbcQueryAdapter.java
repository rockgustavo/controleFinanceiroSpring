package com.controleFinanceiro.infrastructure.adapter.out.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalculosJdbcQueryAdapter implements CalculosQueryPort {

    private final JdbcTemplate jdbc;

    private static final String RENDIMENTO_SQL = """
            WITH totais AS (
                SELECT s.id, s.data, COALESCE(SUM(p.total_liq), 0) AS total_liq
                FROM snapshots s
                LEFT JOIN posicoes p ON p.snapshot_id = s.id
                GROUP BY s.id, s.data
            ),
            atual AS (SELECT * FROM totais WHERE id = ?)
            SELECT
                a.id          AS snapshot_id,
                a.data        AS data_atual,
                a.total_liq   AS total_liq_atual,
                ant.data      AS data_anterior,
                ant.total_liq AS total_liq_anterior
            FROM atual a
            LEFT JOIN totais ant ON ant.data = (
                SELECT MAX(t2.data) FROM totais t2 WHERE t2.data < a.data
            )
            """;

    private static final String ALOCACAO_SQL = """
            SELECT a.tipo, SUM(p.total_liq) AS total_liq
            FROM posicoes p
            JOIN ativos a ON a.id = p.ativo_id
            WHERE p.snapshot_id = ?
            GROUP BY a.tipo
            ORDER BY total_liq DESC
            """;

    private static final String ULTIMO_SNAPSHOT_SQL = """
            SELECT s.data, COALESCE(SUM(p.total_liq), 0) AS total_liq
            FROM snapshots s
            LEFT JOIN posicoes p ON p.snapshot_id = s.id
            WHERE s.data = (SELECT MAX(data) FROM snapshots)
            GROUP BY s.data
            """;

    @Override
    public Optional<DadosRendimento> buscarDadosParaRendimento(UUID snapshotId) {
        return jdbc.query(RENDIMENTO_SQL, this::mapRendimentoRow, snapshotId)
                .stream().findFirst();
    }

    @Override
    public List<AlocacaoBruta> buscarAlocacaoPorTipo(UUID snapshotId) {
        return jdbc.query(ALOCACAO_SQL, this::mapAlocacaoRow, snapshotId);
    }

    @Override
    public Optional<DadosProjecao> buscarDadosUltimoParaProjecao() {
        return jdbc.query(ULTIMO_SNAPSHOT_SQL, this::mapProjecaoRow)
                .stream().findFirst();
    }

    private DadosRendimento mapRendimentoRow(ResultSet rs, int rowNum) throws SQLException {
        return new DadosRendimento(
                UUID.fromString(rs.getString("snapshot_id")),
                rs.getObject("data_atual", LocalDate.class),
                rs.getBigDecimal("total_liq_atual"),
                rs.getObject("data_anterior", LocalDate.class),
                rs.getBigDecimal("total_liq_anterior")
        );
    }

    private AlocacaoBruta mapAlocacaoRow(ResultSet rs, int rowNum) throws SQLException {
        return new AlocacaoBruta(rs.getString("tipo"), rs.getBigDecimal("total_liq"));
    }

    private DadosProjecao mapProjecaoRow(ResultSet rs, int rowNum) throws SQLException {
        return new DadosProjecao(
                rs.getObject("data", LocalDate.class),
                rs.getBigDecimal("total_liq")
        );
    }
}
