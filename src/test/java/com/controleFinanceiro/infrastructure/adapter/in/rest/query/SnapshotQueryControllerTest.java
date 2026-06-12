package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.controleFinanceiro.application.dto.response.PosicaoResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarSnapshotPort;
import com.controleFinanceiro.domain.port.in.query.BuscarUltimoSnapshotPort;
import com.controleFinanceiro.domain.port.in.query.ListarSnapshotsPort;
import com.controleFinanceiro.infrastructure.config.MessageConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SnapshotQueryController.class)
@Import(MessageConfig.class)
class SnapshotQueryControllerTest {

    @Autowired MockMvc mvc;

    @MockitoBean JwtDecoder jwtDecoder;
    @MockitoBean ListarSnapshotsPort listarSnapshots;
    @MockitoBean BuscarSnapshotPort buscarSnapshot;
    @MockitoBean BuscarUltimoSnapshotPort buscarUltimoSnapshot;

    private SnapshotResponse snapshotResponseFake(UUID id) {
        return new SnapshotResponse(id, LocalDate.of(2024, 6, 1), "obs",
                new BigDecimal("500"), new BigDecimal("480"),
                List.of(new PosicaoResponse(UUID.randomUUID(), "Tesouro Selic",
                        "RENDA_FIXA", null, new BigDecimal("10"),
                        new BigDecimal("50"), new BigDecimal("500"), new BigDecimal("480"), null)),
                Instant.now());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void listar_retorna_200_com_resumos() throws Exception {
        var id = UUID.randomUUID();
        when(listarSnapshots.execute()).thenReturn(
                List.of(new SnapshotResumoResponse(id, LocalDate.of(2024, 6, 1),
                        new BigDecimal("480"), 1, Instant.now())));

        mvc.perform(get("/api/snapshots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void buscarUltimo_retorna_200_com_snapshot_completo() throws Exception {
        var id = UUID.randomUUID();
        when(buscarUltimoSnapshot.execute()).thenReturn(snapshotResponseFake(id));

        mvc.perform(get("/api/snapshots/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.posicoes").isArray());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void buscarPorId_retorna_200_com_snapshot() throws Exception {
        var id = UUID.randomUUID();
        when(buscarSnapshot.execute(id)).thenReturn(snapshotResponseFake(id));

        mvc.perform(get("/api/snapshots/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void buscarPorId_retorna_404_quando_nao_encontrado() throws Exception {
        var id = UUID.randomUUID();
        when(buscarSnapshot.execute(any())).thenThrow(
                new NotFoundException("snapshot.nao.encontrado", "SNAPSHOT_NAO_ENCONTRADO", id));

        mvc.perform(get("/api/snapshots/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("SNAPSHOT_NAO_ENCONTRADO"));
    }

    @Test
    void listar_sem_autenticacao_retorna_401() throws Exception {
        mvc.perform(get("/api/snapshots"))
                .andExpect(status().isUnauthorized());
    }
}
