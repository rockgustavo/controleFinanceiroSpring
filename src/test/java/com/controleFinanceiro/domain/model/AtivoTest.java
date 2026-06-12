package com.controleFinanceiro.domain.model;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AtivoTest {

    @Test
    void create_seta_campos_e_timestamps() {
        var ativo = Ativo.create("Tesouro Selic", TipoAtivo.RENDA_FIXA, null, "obs");

        assertThat(ativo.getNome()).isEqualTo("Tesouro Selic");
        assertThat(ativo.getTipo()).isEqualTo(TipoAtivo.RENDA_FIXA);
        assertThat(ativo.getTicker()).isNull();
        assertThat(ativo.getObservacoes()).isEqualTo("obs");
        assertThat(ativo.getId()).isNull();
        assertThat(ativo.getCriadoEm()).isNotNull();
        assertThat(ativo.getAtualizadoEm()).isNotNull();
        assertThat(ativo.estaArquivado()).isFalse();
    }

    @Test
    void arquivar_preenche_arquivadoEm_e_marca_como_arquivado() {
        var ativo = Ativo.create("PETR4", TipoAtivo.RENDA_VARIAVEL, "PETR4", null);

        ativo.arquivar();

        assertThat(ativo.estaArquivado()).isTrue();
        assertThat(ativo.getArquivadoEm()).isNotNull();
        assertThat(ativo.getAtualizadoEm()).isNotNull();
    }

    @Test
    void atualizar_altera_somente_campos_nao_nulos() {
        var ativo = Ativo.create("Nome Antigo", TipoAtivo.RENDA_VARIAVEL, "TICK3", "obs antiga");
        var atualizadoAntes = ativo.getAtualizadoEm();

        ativo.atualizar("Nome Novo", null, "obs nova");

        assertThat(ativo.getNome()).isEqualTo("Nome Novo");
        assertThat(ativo.getTicker()).isEqualTo("TICK3");
        assertThat(ativo.getObservacoes()).isEqualTo("obs nova");
        assertThat(ativo.getAtualizadoEm()).isNotNull();
    }

    @Test
    void atualizar_ticker_quando_informado() {
        var ativo = Ativo.create("Ação", TipoAtivo.RENDA_VARIAVEL, "VELHO3", null);

        ativo.atualizar(null, "NOVO3", null);

        assertThat(ativo.getTicker()).isEqualTo("NOVO3");
        assertThat(ativo.getNome()).isEqualTo("Ação");
    }

    @Test
    void reconstitute_preenche_todos_os_campos() {
        var id = UUID.randomUUID();
        var criadoEm = Instant.now();
        var atualizadoEm = Instant.now();
        var arquivadoEm = Instant.now();

        var ativo = Ativo.reconstitute(id, "Nome", TipoAtivo.FII, "XPML11", "obs",
                criadoEm, atualizadoEm, arquivadoEm);

        assertThat(ativo.getId()).isEqualTo(id);
        assertThat(ativo.getNome()).isEqualTo("Nome");
        assertThat(ativo.getTipo()).isEqualTo(TipoAtivo.FII);
        assertThat(ativo.getTicker()).isEqualTo("XPML11");
        assertThat(ativo.estaArquivado()).isTrue();
    }
}
