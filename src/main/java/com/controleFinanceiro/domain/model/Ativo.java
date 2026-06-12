package com.controleFinanceiro.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Ativo {

    private UUID id;
    private String nome;
    private TipoAtivo tipo;
    private String ticker;
    private String observacoes;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant arquivadoEm;

    private Ativo() {}

    public static Ativo create(String nome, TipoAtivo tipo, String ticker, String observacoes) {
        var ativo = new Ativo();
        ativo.nome = nome;
        ativo.tipo = tipo;
        ativo.ticker = ticker;
        ativo.observacoes = observacoes;
        ativo.criadoEm = Instant.now();
        ativo.atualizadoEm = Instant.now();
        return ativo;
    }

    public static Ativo reconstitute(UUID id, String nome, TipoAtivo tipo, String ticker,
                                     String observacoes, Instant criadoEm, Instant atualizadoEm, Instant arquivadoEm) {
        var ativo = new Ativo();
        ativo.id = id;
        ativo.nome = nome;
        ativo.tipo = tipo;
        ativo.ticker = ticker;
        ativo.observacoes = observacoes;
        ativo.criadoEm = criadoEm;
        ativo.atualizadoEm = atualizadoEm;
        ativo.arquivadoEm = arquivadoEm;
        return ativo;
    }

    public void atualizar(String nome, String ticker, String observacoes) {
        if (nome != null) this.nome = nome;
        if (ticker != null) this.ticker = ticker;
        if (observacoes != null) this.observacoes = observacoes;
        this.atualizadoEm = Instant.now();
    }

    public void arquivar() {
        this.arquivadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public boolean estaArquivado() { return arquivadoEm != null; }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public TipoAtivo getTipo() { return tipo; }
    public String getTicker() { return ticker; }
    public String getObservacoes() { return observacoes; }
    public Instant getCriadoEm() { return criadoEm; }
    public Instant getAtualizadoEm() { return atualizadoEm; }
    public Instant getArquivadoEm() { return arquivadoEm; }
}
