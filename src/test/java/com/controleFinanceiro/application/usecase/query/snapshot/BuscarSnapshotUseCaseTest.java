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
class BuscarSnapshotUseCaseTest {

    @Mock SnapshotQueryPort queryPort;
    @InjectMocks BuscarSnapshotUseCase useCase;

    @Test
    void execute_retorna_snapshot_quando_encontrado() {
        var id = UUID.randomUUID();
        var response = new SnapshotResponse(id, LocalDate.of(2025, 5, 31), "obs",
                new BigDecimal("67650"), new BigDecimal("66300"), List.of(), Instant.now());
        when(queryPort.findById(id)).thenReturn(Optional.of(response));

        var resultado = useCase.execute(id);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.totalLiq()).isEqualByComparingTo(new BigDecimal("66300"));
    }

    @Test
    void execute_lanca_NotFoundException_quando_nao_encontrado() {
        var id = UUID.randomUUID();
        when(queryPort.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
    }
}
