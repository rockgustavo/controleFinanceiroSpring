package com.controleFinanceiro.infrastructure.adapter.out.query;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AtivoJdbcQueryAdapter implements AtivoQueryPort {

    private final JdbcTemplate jdbc;

    private static final String FIND_ALL = """
            SELECT id, nome, tipo, ticker, observacoes
            FROM ativos
            WHERE arquivado_em IS NULL
            ORDER BY nome
            """;

    private static final String FIND_BY_ID = """
            SELECT id, nome, tipo, ticker, observacoes
            FROM ativos
            WHERE id = ? AND arquivado_em IS NULL
            """;

    @Override
    public List<AtivoResponse> findAllActive() {
        return jdbc.query(FIND_ALL, this::mapRow);
    }

    @Override
    public Optional<AtivoResponse> findActiveById(UUID id) {
        return jdbc.query(FIND_BY_ID, this::mapRow, id).stream().findFirst();
    }

    private AtivoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AtivoResponse(
                UUID.fromString(rs.getString("id")),
                rs.getString("nome"),
                rs.getString("tipo"),
                rs.getString("ticker"),
                rs.getString("observacoes")
        );
    }
}
