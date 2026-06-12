package com.controleFinanceiro.application.usecase.command.snapshot;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtivosAtivosHandlerTest {

    @Mock AtivoRepositoryPort ativoRepository;

    private CriarSnapshotCommand commandComAtivo(UUID ativoId) {
        var posicao = new CriarSnapshotCommand.PosicaoInput(
                ativoId, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        return new CriarSnapshotCommand(LocalDate.now(), null, List.of(posicao));
    }

    @Test
    void handle_ativo_nao_encontrado_lanca_NotFoundException() {
        var id = UUID.randomUUID();
        when(ativoRepository.findById(id)).thenReturn(Optional.empty());
        var handler = new AtivosAtivosHandler(ativoRepository);

        assertThatThrownBy(() -> handler.handle(commandComAtivo(id)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void handle_ativo_arquivado_lanca_DomainException() {
        var id = UUID.randomUUID();
        var arquivado = Ativo.reconstitute(id, "Ativo", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), Instant.now());
        when(ativoRepository.findById(id)).thenReturn(Optional.of(arquivado));
        var handler = new AtivosAtivosHandler(ativoRepository);

        assertThatThrownBy(() -> handler.handle(commandComAtivo(id)))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void handle_ativo_ativo_passa_sem_excecao() {
        var id = UUID.randomUUID();
        var ativo = Ativo.reconstitute(id, "Ativo", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
        when(ativoRepository.findById(id)).thenReturn(Optional.of(ativo));
        var handler = new AtivosAtivosHandler(ativoRepository);

        assertThatCode(() -> handler.handle(commandComAtivo(id))).doesNotThrowAnyException();
    }
}
