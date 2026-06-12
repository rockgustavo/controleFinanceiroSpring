package com.controleFinanceiro.application.usecase.command.snapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.port.out.command.SnapshotRepositoryPort;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataUnicaHandlerTest {

    @Mock SnapshotRepositoryPort repository;

    private CriarSnapshotCommand commandParaData(LocalDate data) {
        var posicao = new CriarSnapshotCommand.PosicaoInput(
                UUID.randomUUID(), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null);
        return new CriarSnapshotCommand(data, null, List.of(posicao));
    }

    @Test
    void handle_data_ja_existente_lanca_ConflictException() {
        var data = LocalDate.of(2024, 6, 1);
        when(repository.existsByData(data)).thenReturn(true);
        var handler = new DataUnicaHandler(repository);

        assertThatThrownBy(() -> handler.handle(commandParaData(data)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void handle_data_nova_passa_sem_excecao() {
        var data = LocalDate.of(2024, 6, 1);
        when(repository.existsByData(data)).thenReturn(false);
        var handler = new DataUnicaHandler(repository);

        assertThatCode(() -> handler.handle(commandParaData(data))).doesNotThrowAnyException();
    }
}
