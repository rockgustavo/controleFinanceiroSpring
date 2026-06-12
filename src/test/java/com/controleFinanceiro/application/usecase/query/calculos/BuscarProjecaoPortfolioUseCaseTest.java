package com.controleFinanceiro.application.usecase.query.calculos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort.DadosProjecao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarProjecaoPortfolioUseCaseTest {

    @Mock CalculosQueryPort calculosQuery;
    @InjectMocks BuscarProjecaoPortfolioUseCase useCase;

    private final DadosProjecao dadosStub =
            new DadosProjecao(LocalDate.of(2025, 5, 31), new BigDecimal("10000"));

    @Test
    void execute_calcula_projecao_com_juros_compostos() {
        when(calculosQuery.buscarDadosUltimoParaProjecao()).thenReturn(Optional.of(dadosStub));

        var resultado = useCase.execute(new BigDecimal("1"), 12);

        assertThat(resultado.totalLiqAtual()).isEqualByComparingTo(new BigDecimal("10000"));
        assertThat(resultado.totalProjetado()).isGreaterThan(new BigDecimal("10000"));
        assertThat(resultado.rendimentoProjetado()).isGreaterThan(BigDecimal.ZERO);
        assertThat(resultado.meses()).isEqualTo(12);
    }

    @Test
    void execute_rendimento_projetado_e_diferenca_entre_total_e_inicial() {
        when(calculosQuery.buscarDadosUltimoParaProjecao()).thenReturn(Optional.of(dadosStub));

        var resultado = useCase.execute(new BigDecimal("10"), 1);

        assertThat(resultado.rendimentoProjetado())
                .isEqualByComparingTo(resultado.totalProjetado().subtract(resultado.totalLiqAtual()));
    }

    @Test
    void execute_lanca_ValidationException_quando_taxa_zero() {
        assertThatThrownBy(() -> useCase.execute(BigDecimal.ZERO, 12))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void execute_lanca_ValidationException_quando_taxa_negativa() {
        assertThatThrownBy(() -> useCase.execute(new BigDecimal("-1"), 12))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void execute_lanca_ValidationException_quando_meses_zero() {
        assertThatThrownBy(() -> useCase.execute(new BigDecimal("1"), 0))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void execute_lanca_ValidationException_quando_meses_acima_de_600() {
        assertThatThrownBy(() -> useCase.execute(new BigDecimal("1"), 601))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void execute_aceita_meses_no_limite_de_600() {
        when(calculosQuery.buscarDadosUltimoParaProjecao()).thenReturn(Optional.of(dadosStub));

        var resultado = useCase.execute(new BigDecimal("0.1"), 600);

        assertThat(resultado.meses()).isEqualTo(600);
    }

    @Test
    void execute_lanca_NotFoundException_quando_nenhum_snapshot_existe() {
        when(calculosQuery.buscarDadosUltimoParaProjecao()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new BigDecimal("1"), 12))
                .isInstanceOf(NotFoundException.class);
    }
}
