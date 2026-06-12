package com.controleFinanceiro.application.usecase.command.snapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.controleFinanceiro.application.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CriarSnapshotCommandTest {

    private CriarSnapshotCommand.PosicaoInput posicaoValida() {
        return new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), new BigDecimal("10"), new BigDecimal("50.00"),
                new BigDecimal("480"), null);
    }

    @Test
    void construtor_lanca_excecao_quando_data_nula() {
        assertThatThrownBy(() -> new CriarSnapshotCommand(null, null, List.of(posicaoValida())))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void construtor_lanca_excecao_quando_posicoes_vazias() {
        assertThatThrownBy(() -> new CriarSnapshotCommand(LocalDate.now(), null, List.of()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void construtor_lanca_excecao_quando_posicoes_nulas() {
        assertThatThrownBy(() -> new CriarSnapshotCommand(LocalDate.now(), null, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void posicaoInput_lanca_excecao_quando_ativoId_nulo() {
        assertThatThrownBy(() -> new CriarSnapshotCommand.PosicaoInput(
                null, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void posicaoInput_lanca_excecao_quando_quantidade_zero() {
        assertThatThrownBy(() -> new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), BigDecimal.ZERO, new BigDecimal("50"), new BigDecimal("480"), null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void posicaoInput_lanca_excecao_quando_quantidade_negativa() {
        assertThatThrownBy(() -> new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), new BigDecimal("-1"), new BigDecimal("50"), new BigDecimal("480"), null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void posicaoInput_lanca_excecao_quando_preco_zero() {
        assertThatThrownBy(() -> new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void posicaoInput_lanca_excecao_quando_totalLiq_negativo() {
        assertThatThrownBy(() -> new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ONE, new BigDecimal("-0.01"), null))
                .isInstanceOf(ValidationException.class);
    }
}
