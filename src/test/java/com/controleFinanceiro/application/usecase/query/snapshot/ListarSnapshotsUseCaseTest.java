package com.controleFinanceiro.application.usecase.query.snapshot;

import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarSnapshotsUseCaseTest {

    @Mock SnapshotQueryPort queryPort;
    @InjectMocks ListarSnapshotsUseCase useCase;

    @Test
    void execute_retorna_lista_de_snapshots() {
        var snapshots = List.of(
                new SnapshotResumoResponse(UUID.randomUUID(), LocalDate.of(2025, 5, 31),
                        new BigDecimal("66300"), 5, Instant.now()),
                new SnapshotResumoResponse(UUID.randomUUID(), LocalDate.of(2025, 4, 30),
                        new BigDecimal("65000"), 5, Instant.now())
        );
        when(queryPort.findAll()).thenReturn(snapshots);

        var resultado = useCase.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).data()).isEqualTo(LocalDate.of(2025, 5, 31));
    }

    @Test
    void execute_retorna_lista_vazia_quando_sem_snapshots() {
        when(queryPort.findAll()).thenReturn(List.of());

        assertThat(useCase.execute()).isEmpty();
    }
}
