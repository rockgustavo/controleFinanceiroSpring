package com.controleFinanceiro.application.usecase.command.ativo;

import com.controleFinanceiro.application.exception.ValidationException;
import com.controleFinanceiro.domain.model.TipoAtivo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CriarAtivoCommandTest {

    @Test
    void construtor_valida_nome_nulo() {
        assertThatThrownBy(() -> new CriarAtivoCommand(null, TipoAtivo.RENDA_FIXA, null, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void construtor_valida_tipo_nulo() {
        assertThatThrownBy(() -> new CriarAtivoCommand("Tesouro", null, null, null))
                .isInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @EnumSource(value = TipoAtivo.class, names = {"RENDA_VARIAVEL", "FII", "ETF"})
    void construtor_exige_ticker_para_tipos_variaveis(TipoAtivo tipo) {
        assertThatThrownBy(() -> new CriarAtivoCommand("Ativo", tipo, null, null))
                .isInstanceOf(ValidationException.class);

        assertThatThrownBy(() -> new CriarAtivoCommand("Ativo", tipo, "  ", null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void construtor_aceita_renda_fixa_sem_ticker() {
        var command = new CriarAtivoCommand("CDB Banco X", TipoAtivo.RENDA_FIXA, null, "obs");
        assertThat(command.nome()).isEqualTo("CDB Banco X");
        assertThat(command.ticker()).isNull();
    }

    @ParameterizedTest
    @EnumSource(value = TipoAtivo.class, names = {"RENDA_VARIAVEL", "FII", "ETF"})
    void construtor_aceita_tipos_variaveis_com_ticker(TipoAtivo tipo) {
        var command = new CriarAtivoCommand("Ativo", tipo, "TICK3", null);
        assertThat(command.ticker()).isEqualTo("TICK3");
    }
}
