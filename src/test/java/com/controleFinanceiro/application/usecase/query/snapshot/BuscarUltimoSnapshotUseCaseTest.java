package com.controleFinanceiro.application.usecase.query.snapshot;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarUltimoSnapshotUseCaseTest {

    @Mock SnapshotQueryPort queryPort;
    @InjectMocks BuscarUltimoSnapshotUseCase useCase;

    @Test
    void execute_retorna_ultimo_snapshot() {
        var id = UUID.randomUUID();
        var response = new SnapshotResponse(id, LocalDate.of(2025, 5, 31), null,
                BigDecimal.ZERO, new BigDecimal("66300"), List.of(), Instant.now());
        when(queryPort.findLatest()).thenReturn(Optional.of(response));

        var resultado = useCase.execute();

        assertThat(resultado).isNotNull();
        assertThat(resultado.data()).isEqualTo(LocalDate.of(2025, 5, 31));
    }

    @Test
    void execute_lanca_NotFoundException_quando_sem_snapshots() {
        when(queryPort.findLatest()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute())
                .isInstanceOf(NotFoundException.class);
    }
}
