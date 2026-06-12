package com.controleFinanceiro.application.usecase.query.ativo;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAtivosUseCaseTest {

    @Mock AtivoQueryPort queryPort;
    @InjectMocks ListarAtivosUseCase useCase;

    @Test
    void execute_retorna_lista_de_ativos() {
        var ativos = List.of(
                new AtivoResponse(UUID.randomUUID(), "Tesouro Selic", "RENDA_FIXA", null, null),
                new AtivoResponse(UUID.randomUUID(), "PETR4", "RENDA_VARIAVEL", "PETR4", null)
        );
        when(queryPort.findAllActive()).thenReturn(ativos);

        var resultado = useCase.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("Tesouro Selic");
    }

    @Test
    void execute_retorna_lista_vazia_quando_sem_ativos() {
        when(queryPort.findAllActive()).thenReturn(List.of());

        assertThat(useCase.execute()).isEmpty();
    }
}
