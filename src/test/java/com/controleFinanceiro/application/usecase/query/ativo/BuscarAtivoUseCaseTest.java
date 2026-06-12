package com.controleFinanceiro.application.usecase.query.ativo;

import com.controleFinanceiro.application.dto.response.AtivoResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.query.AtivoQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarAtivoUseCaseTest {

    @Mock AtivoQueryPort queryPort;
    @InjectMocks BuscarAtivoUseCase useCase;

    @Test
    void execute_retorna_ativo_quando_encontrado() {
        var id = UUID.randomUUID();
        var response = new AtivoResponse(id, "Tesouro Selic", "RENDA_FIXA", null, null);
        when(queryPort.findActiveById(id)).thenReturn(Optional.of(response));

        var resultado = useCase.execute(id);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.nome()).isEqualTo("Tesouro Selic");
    }

    @Test
    void execute_lanca_NotFoundException_quando_nao_encontrado() {
        var id = UUID.randomUUID();
        when(queryPort.findActiveById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
    }
}
