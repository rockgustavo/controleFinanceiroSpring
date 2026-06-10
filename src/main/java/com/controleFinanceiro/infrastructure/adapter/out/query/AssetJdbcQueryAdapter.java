package com.controleFinanceiro.infrastructure.adapter.out.query;

import com.controleFinanceiro.application.dto.response.AssetResponse;
import com.controleFinanceiro.domain.port.out.query.AssetQueryPort;
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
public class AssetJdbcQueryAdapter implements AssetQueryPort {

    private final JdbcTemplate jdbc;

    private static final String FIND_ALL = """
            SELECT id, name, type, ticker, notes
            FROM assets
            WHERE deleted_at IS NULL
            ORDER BY name
            """;

    private static final String FIND_BY_ID = """
            SELECT id, name, type, ticker, notes
            FROM assets
            WHERE id = ? AND deleted_at IS NULL
            """;

    @Override
    public List<AssetResponse> findAllActive() {
        return jdbc.query(FIND_ALL, this::mapRow);
    }

    @Override
    public Optional<AssetResponse> findActiveById(UUID id) {
        return jdbc.query(FIND_BY_ID, this::mapRow, id).stream().findFirst();
    }

    private AssetResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AssetResponse(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("type"),
                rs.getString("ticker"),
                rs.getString("notes")
        );
    }
}
