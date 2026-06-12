package com.controleFinanceiro.application.usecase.command.ativo;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarAtivoUseCaseTest {

    @Mock AtivoRepositoryPort repository;
    @Mock EventPublisherPort eventPublisher;
    @InjectMocks AtualizarAtivoUseCase useCase;

    @Test
    void execute_atualiza_ativo_e_publica_evento() {
        var id = UUID.randomUUID();
        var ativo = Ativo.reconstitute(id, "Nome Antigo", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
        when(repository.findActiveById(id)).thenReturn(Optional.of(ativo));
        when(repository.save(any())).thenReturn(ativo);

        var resultado = useCase.execute(id, new AtualizarAtivoCommand("Nome Novo", null, null));

        assertThat(resultado).isNotNull();
        verify(repository).save(any());
        verify(eventPublisher).publish(any(AtivoAlteradoEvent.class));
    }

    @Test
    void execute_ativo_nao_encontrado_lanca_NotFoundException() {
        var id = UUID.randomUUID();
        when(repository.findActiveById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, new AtualizarAtivoCommand("Nome", null, null)))
                .isInstanceOf(NotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void execute_ticker_em_uso_por_outro_ativo_lanca_ConflictException() {
        var id = UUID.randomUUID();
        var ativo = Ativo.reconstitute(id, "Nome", TipoAtivo.RENDA_VARIAVEL,
                "VELHO3", null, Instant.now(), Instant.now(), null);
        when(repository.findActiveById(id)).thenReturn(Optional.of(ativo));
        when(repository.existsByTickerAndNotArchivedExcluding("NOVO3", id)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(id, new AtualizarAtivoCommand(null, "NOVO3", null)))
                .isInstanceOf(ConflictException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void execute_mesmo_ticker_nao_verifica_conflito() {
        var id = UUID.randomUUID();
        var ativo = Ativo.reconstitute(id, "Nome", TipoAtivo.RENDA_VARIAVEL,
                "PETR4", null, Instant.now(), Instant.now(), null);
        when(repository.findActiveById(id)).thenReturn(Optional.of(ativo));
        when(repository.save(any())).thenReturn(ativo);

        var resultado = useCase.execute(id, new AtualizarAtivoCommand("Nome Novo", "PETR4", null));

        assertThat(resultado).isNotNull();
        verify(repository, never()).existsByTickerAndNotArchivedExcluding(any(), any());
    }
}
