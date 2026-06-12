package com.controleFinanceiro.infrastructure.adapter.in.rest.command;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
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

import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.in.command.ArquivarAtivoPort;
import com.controleFinanceiro.domain.port.in.command.AtualizarAtivoPort;
import com.controleFinanceiro.domain.port.in.command.CriarAtivoPort;
import com.controleFinanceiro.infrastructure.config.MessageConfig;
import com.controleFinanceiro.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AtivoCommandController.class)
@Import({MessageConfig.class, SecurityConfig.class})
class AtivoCommandControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean JwtDecoder jwtDecoder;
    @MockitoBean CriarAtivoPort criarAtivo;
    @MockitoBean AtualizarAtivoPort atualizarAtivo;
    @MockitoBean ArquivarAtivoPort arquivarAtivo;

    private Ativo ativoFake(UUID id) {
        return Ativo.reconstitute(id, "Tesouro Selic", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_retorna_201_com_ativo() throws Exception {
        var id = UUID.randomUUID();
        when(criarAtivo.execute(any())).thenReturn(ativoFake(id));

        var body = Map.of("nome", "Tesouro Selic", "tipo", "RENDA_FIXA");

        mvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Tesouro Selic"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_retorna_200_com_ativo() throws Exception {
        var id = UUID.randomUUID();
        when(atualizarAtivo.execute(eq(id), any())).thenReturn(ativoFake(id));

        var body = Map.of("nome", "Nome Atualizado");

        mvc.perform(patch("/api/assets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void arquivar_retorna_204() throws Exception {
        var id = UUID.randomUUID();

        mvc.perform(delete("/api/assets/{id}", id))
                .andExpect(status().isNoContent());

        verify(arquivarAtivo).execute(id);
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void criar_com_role_viewer_retorna_403() throws Exception {
        var body = Map.of("nome", "Tesouro Selic", "tipo", "RENDA_FIXA");

        mvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body))))
                .andExpect(status().isForbidden());
    }

    @Test
    void criar_sem_autenticacao_retorna_401() throws Exception {
        var body = Map.of("nome", "Tesouro Selic", "tipo", "RENDA_FIXA");

        mvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body))))
                .andExpect(status().isUnauthorized());
    }
}
