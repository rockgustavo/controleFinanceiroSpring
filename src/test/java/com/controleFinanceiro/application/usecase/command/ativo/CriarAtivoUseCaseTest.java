package com.controleFinanceiro.application.usecase.command.ativo;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarAtivoUseCaseTest {

    @Mock AtivoRepositoryPort repository;
    @Mock EventPublisherPort eventPublisher;
    @InjectMocks CriarAtivoUseCase useCase;

    @Test
    void execute_cria_ativo_e_publica_evento() {
        var command = new CriarAtivoCommand("Tesouro Selic", TipoAtivo.RENDA_FIXA, null, null);
        var ativoSalvo = Ativo.reconstitute(UUID.randomUUID(), "Tesouro Selic",
                TipoAtivo.RENDA_FIXA, null, null, Instant.now(), Instant.now(), null);
        when(repository.save(any())).thenReturn(ativoSalvo);

        var resultado = useCase.execute(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        verify(repository).save(any());
        verify(eventPublisher).publish(any(AtivoAlteradoEvent.class));
    }

    @Test
    void execute_com_ticker_duplicado_lanca_ConflictException_sem_salvar() {
        var command = new CriarAtivoCommand("Ação X", TipoAtivo.RENDA_VARIAVEL, "PETR4", null);
        when(repository.existsByTickerAndNotArchived("PETR4")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(ConflictException.class);

        verify(repository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void execute_renda_fixa_sem_ticker_nao_lanca_excecao() {
        var command = new CriarAtivoCommand("CDB Banco X", TipoAtivo.RENDA_FIXA, null, "obs");
        var ativoSalvo = Ativo.reconstitute(UUID.randomUUID(), "CDB Banco X",
                TipoAtivo.RENDA_FIXA, null, "obs", Instant.now(), Instant.now(), null);
        when(repository.save(any())).thenReturn(ativoSalvo);

        var resultado = useCase.execute(command);

        assertThat(resultado).isNotNull();
    }
}
