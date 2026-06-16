package com.controleFinanceiro.application.usecase.command.snapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.controleFinanceiro.domain.exception.DomainException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SemPosicoesDuplicadasHandlerTest {

    private final SemPosicoesDuplicadasHandler handler = new SemPosicoesDuplicadasHandler();

    @Test
    void handle_ativoId_duplicado_lanca_DomainException() {
        var id = UUID.randomUUID();
        var posicao1 = new CriarSnapshotCommand.PosicaoInput(id, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        var posicao2 = new CriarSnapshotCommand.PosicaoInput(id, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, null);
        var command = new CriarSnapshotCommand(LocalDate.now(), null, List.of(posicao1, posicao2));

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void handle_ativos_distintos_passa_sem_excecao() {
        var posicao1 = new CriarSnapshotCommand.PosicaoInput(UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        var posicao2 = new CriarSnapshotCommand.PosicaoInput(UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        var command = new CriarSnapshotCommand(LocalDate.now(), null, List.of(posicao1, posicao2));

        assertThatCode(() -> handler.handle(command)).doesNotThrowAnyException();
    }

    @Test
    void handle_posicao_unica_passa_sem_excecao() {
        var posicao = new CriarSnapshotCommand.PosicaoInput(UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        var command = new CriarSnapshotCommand(LocalDate.now(), null, List.of(posicao));

        assertThatCode(() -> handler.handle(command)).doesNotThrowAnyException();
    }
}
