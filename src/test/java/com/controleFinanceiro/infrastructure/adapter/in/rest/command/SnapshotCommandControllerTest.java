package com.controleFinanceiro.infrastructure.adapter.in.rest.command;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.model.Posicao;
import com.controleFinanceiro.domain.model.Snapshot;
import com.controleFinanceiro.domain.port.in.command.CriarSnapshotPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import com.controleFinanceiro.infrastructure.config.MessageConfig;
import com.controleFinanceiro.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SnapshotCommandController.class)
@Import({MessageConfig.class, SecurityConfig.class})
class SnapshotCommandControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean JwtDecoder jwtDecoder;
    @MockitoBean CriarSnapshotPort criarSnapshot;
    @MockitoBean SnapshotQueryPort snapshotQueryPort;

    private Map<String, Object> requestValido() {
        return Map.of(
                "data", "2024-06-01",
                "observacoes", "obs",
                "posicoes", List.of(Map.of(
                        "ativoId", UUID.randomUUID().toString(),
                        "quantidade", 10,
                        "precoUnit", 50.00,
                        "totalLiq", 480.00
                ))
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_retorna_201_com_snapshot() throws Exception {
        var snapshotId = UUID.randomUUID();
        var posicao = Posicao.of(UUID.randomUUID(), new BigDecimal("10"), new BigDecimal("50"),
                new BigDecimal("480"), null);
        var snapshotSalvo = Snapshot.reconstitute(snapshotId, LocalDate.of(2024, 6, 1),
                "obs", List.of(posicao), Instant.now());
        var snapshotResponse = new SnapshotResponse(snapshotId, LocalDate.of(2024, 6, 1),
                "obs", new BigDecimal("500"), new BigDecimal("480"), List.of(), Instant.now());

        when(criarSnapshot.execute(any())).thenReturn(snapshotSalvo);
        when(snapshotQueryPort.findById(snapshotId)).thenReturn(Optional.of(snapshotResponse));

        mvc.perform(post("/api/snapshots")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(requestValido()))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(snapshotId.toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_data_duplicada_retorna_409() throws Exception {
        when(criarSnapshot.execute(any())).thenThrow(
                new ConflictException("snapshot.data.existente", "SNAPSHOT_DATA_EXISTENTE", LocalDate.of(2024, 6, 1)));

        mvc.perform(post("/api/snapshots")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(requestValido()))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("SNAPSHOT_DATA_EXISTENTE"));
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void criar_com_role_viewer_retorna_403() throws Exception {
        mvc.perform(post("/api/snapshots")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(requestValido()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void criar_sem_autenticacao_retorna_401() throws Exception {
        mvc.perform(post("/api/snapshots")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(requestValido()))))
                .andExpect(status().isUnauthorized());
    }
}
