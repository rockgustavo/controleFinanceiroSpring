package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

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

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.in.query.BuscarAtivoPort;
import com.controleFinanceiro.domain.port.in.query.ListarAtivosPort;
import com.controleFinanceiro.infrastructure.config.MessageConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtivoQueryController.class)
@Import(MessageConfig.class)
class AtivoQueryControllerTest {

    @Autowired MockMvc mvc;

    @MockitoBean JwtDecoder jwtDecoder;
    @MockitoBean ListarAtivosPort listarAtivos;
    @MockitoBean BuscarAtivoPort buscarAtivo;

    @Test
    @WithMockUser(roles = "VIEWER")
    void listar_retorna_200_com_lista() throws Exception {
        var id = UUID.randomUUID();
        when(listarAtivos.execute()).thenReturn(
                List.of(new AtivoResponse(id, "Tesouro Selic", "RENDA_FIXA", null, null)));

        mvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nome").value("Tesouro Selic"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_retorna_200_com_admin() throws Exception {
        when(listarAtivos.execute()).thenReturn(List.of());

        mvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void buscarPorId_retorna_200_quando_encontrado() throws Exception {
        var id = UUID.randomUUID();
        when(buscarAtivo.execute(id)).thenReturn(
                new AtivoResponse(id, "PETR4 Ação", "RENDA_VARIAVEL", "PETR4", null));

        mvc.perform(get("/api/assets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ticker").value("PETR4"));
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void buscarPorId_retorna_404_quando_nao_encontrado() throws Exception {
        var id = UUID.randomUUID();
        when(buscarAtivo.execute(any())).thenThrow(
                new NotFoundException("ativo.nao.encontrado", "ATIVO_NAO_ENCONTRADO", id));

        mvc.perform(get("/api/assets/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("ATIVO_NAO_ENCONTRADO"));
    }

    @Test
    void listar_sem_autenticacao_retorna_401() throws Exception {
        mvc.perform(get("/api/assets"))
                .andExpect(status().isUnauthorized());
    }
}
