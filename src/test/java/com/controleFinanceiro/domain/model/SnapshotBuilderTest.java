package com.controleFinanceiro.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.controleFinanceiro.domain.exception.DomainException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnapshotBuilderTest {

    private Posicao posicaoQualquer() {
        return Posicao.of(UUID.randomUUID(), new BigDecimal("10"), new BigDecimal("50"),
                new BigDecimal("480"), null);
    }

    @Test
    void build_cria_snapshot_com_campos_e_posicoes() {
        var data = LocalDate.of(2024, 6, 1);

        var snapshot = Snapshot.builder()
                .data(data)
                .observacoes("obs de teste")
                .posicao(posicaoQualquer())
                .posicao(posicaoQualquer())
                .build();

        assertThat(snapshot.getData()).isEqualTo(data);
        assertThat(snapshot.getObservacoes()).isEqualTo("obs de teste");
        assertThat(snapshot.getPosicoes()).hasSize(2);
        assertThat(snapshot.getCriadoEm()).isNotNull();
        assertThat(snapshot.getId()).isNull();
    }

    @Test
    void build_sem_posicoes_lanca_DomainException() {
        var builder = Snapshot.builder().data(LocalDate.now());

        assertThatThrownBy(builder::build)
                .isInstanceOf(DomainException.class);
    }

    @Test
    void build_sem_data_lanca_NullPointerException() {
        var builder = Snapshot.builder().posicao(posicaoQualquer());

        assertThatThrownBy(builder::build)
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void posicoes_sao_imutaveis_apos_build() {
        var snapshot = Snapshot.builder()
                .data(LocalDate.now())
                .posicao(posicaoQualquer())
                .build();

        assertThatThrownBy(() -> snapshot.getPosicoes().add(posicaoQualquer()))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
