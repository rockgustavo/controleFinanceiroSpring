package com.controleFinanceiro.application.usecase.query.calculos;

import com.controleFinanceiro.application.dto.response.SnapshotResponse;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort.AlocacaoBruta;
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
class BuscarAlocacaoSnapshotUseCaseTest {

    @Mock CalculosQueryPort calculosQuery;
    @Mock SnapshotQueryPort snapshotQuery;
    @InjectMocks BuscarAlocacaoSnapshotUseCase useCase;

    private final UUID id = UUID.randomUUID();

    private SnapshotResponse snapshotStub() {
        return new SnapshotResponse(id, LocalDate.now(), null,
                BigDecimal.ZERO, new BigDecimal("100000"), List.of(), Instant.now());
    }

    @Test
    void execute_calcula_percentuais_corretamente() {
        when(snapshotQuery.findById(id)).thenReturn(Optional.of(snapshotStub()));
        when(calculosQuery.buscarAlocacaoPorTipo(id)).thenReturn(List.of(
                new AlocacaoBruta("RENDA_FIXA", new BigDecimal("70000")),
                new AlocacaoBruta("RENDA_VARIAVEL", new BigDecimal("30000"))
        ));

        var resultado = useCase.execute(id);

        assertThat(resultado).hasSize(2);
        var rendaFixa = resultado.stream().filter(a -> a.tipo().equals("RENDA_FIXA")).findFirst().orElseThrow();
        assertThat(rendaFixa.percentual()).isEqualByComparingTo(new BigDecimal("70.00"));

        var rendaVariavel = resultado.stream().filter(a -> a.tipo().equals("RENDA_VARIAVEL")).findFirst().orElseThrow();
        assertThat(rendaVariavel.percentual()).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void execute_retorna_percentual_zero_quando_total_zerado() {
        when(snapshotQuery.findById(id)).thenReturn(Optional.of(snapshotStub()));
        when(calculosQuery.buscarAlocacaoPorTipo(id)).thenReturn(List.of(
                new AlocacaoBruta("RENDA_FIXA", BigDecimal.ZERO)
        ));

        var resultado = useCase.execute(id);

        assertThat(resultado.get(0).percentual()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void execute_lanca_NotFoundException_quando_snapshot_nao_existe() {
        when(snapshotQuery.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
    }
}
