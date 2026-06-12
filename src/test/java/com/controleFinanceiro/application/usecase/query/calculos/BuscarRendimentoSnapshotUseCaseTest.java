package com.controleFinanceiro.application.usecase.query.calculos;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort;
import com.controleFinanceiro.domain.port.out.query.CalculosQueryPort.DadosRendimento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarRendimentoSnapshotUseCaseTest {

    @Mock CalculosQueryPort calculosQuery;
    @InjectMocks BuscarRendimentoSnapshotUseCase useCase;

    private final UUID snapshotId = UUID.randomUUID();
    private final LocalDate dataAtual = LocalDate.of(2025, 5, 31);
    private final LocalDate dataAnterior = LocalDate.of(2025, 4, 30);

    @Test
    void execute_modo_simples_calcula_rendimento_absoluto_e_percentual() {
        var dados = new DadosRendimento(snapshotId, dataAtual,
                new BigDecimal("110"), dataAnterior, new BigDecimal("100"));
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.of(dados));

        var resultado = useCase.execute(snapshotId, "simple");

        assertThat(resultado.rendimentoAbsoluto()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(resultado.rendimentoPercentual()).isEqualByComparingTo(new BigDecimal("10.0000"));
        assertThat(resultado.modo()).isEqualTo("simple");
    }

    @Test
    void execute_modo_cagr_anualiza_rendimento() {
        // 1 mês entre datas, 10% de ganho → CAGR anualiza para ~213%
        var dados = new DadosRendimento(snapshotId, dataAtual,
                new BigDecimal("110"), dataAnterior, new BigDecimal("100"));
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.of(dados));

        var resultado = useCase.execute(snapshotId, "cagr");

        assertThat(resultado.modo()).isEqualTo("cagr");
        assertThat(resultado.rendimentoPercentual()).isGreaterThan(new BigDecimal("10"));
    }

    @Test
    void execute_modo_invalido_trata_como_simples() {
        var dados = new DadosRendimento(snapshotId, dataAtual,
                new BigDecimal("110"), dataAnterior, new BigDecimal("100"));
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.of(dados));

        var resultado = useCase.execute(snapshotId, "xpto");

        assertThat(resultado.modo()).isEqualTo("simple");
    }

    @Test
    void execute_lanca_NotFoundException_quando_snapshot_nao_existe() {
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(snapshotId, "simple"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void execute_lanca_DomainException_quando_sem_historico_anterior() {
        var dados = new DadosRendimento(snapshotId, dataAtual,
                new BigDecimal("110"), null, null);
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.of(dados));

        assertThatThrownBy(() -> useCase.execute(snapshotId, "simple"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void execute_lanca_DomainException_quando_total_anterior_zero() {
        var dados = new DadosRendimento(snapshotId, dataAtual,
                new BigDecimal("110"), dataAnterior, BigDecimal.ZERO);
        when(calculosQuery.buscarDadosParaRendimento(snapshotId)).thenReturn(Optional.of(dados));

        assertThatThrownBy(() -> useCase.execute(snapshotId, "simple"))
                .isInstanceOf(DomainException.class);
    }
}
