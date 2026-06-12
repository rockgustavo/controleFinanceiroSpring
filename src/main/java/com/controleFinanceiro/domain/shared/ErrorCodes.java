package com.controleFinanceiro.domain.shared;

public final class ErrorCodes {

    private ErrorCodes() {}

    public static final String ATIVO_NAO_ENCONTRADO = "ATIVO_NAO_ENCONTRADO";
    public static final String TICKER_OBRIGATORIO = "TICKER_OBRIGATORIO";
    public static final String TICKER_JA_CADASTRADO = "TICKER_JA_CADASTRADO";
    public static final String NOME_OBRIGATORIO = "NOME_OBRIGATORIO";
    public static final String TIPO_OBRIGATORIO = "TIPO_OBRIGATORIO";

    public static final String SNAPSHOT_NAO_ENCONTRADO = "SNAPSHOT_NAO_ENCONTRADO";
    public static final String NENHUM_SNAPSHOT = "NENHUM_SNAPSHOT";
    public static final String SNAPSHOT_DATA_EXISTENTE = "SNAPSHOT_DATA_EXISTENTE";
    public static final String SNAPSHOT_DATA_OBRIGATORIA = "SNAPSHOT_DATA_OBRIGATORIA";
    public static final String SNAPSHOT_SEM_POSICOES = "SNAPSHOT_SEM_POSICOES";
    public static final String ATIVO_ARQUIVADO = "ATIVO_ARQUIVADO";
    public static final String POSICAO_DUPLICADA = "POSICAO_DUPLICADA";

    public static final String POSICAO_ATIVO_OBRIGATORIO = "POSICAO_ATIVO_OBRIGATORIO";
    public static final String POSICAO_QUANTIDADE_INVALIDA = "POSICAO_QUANTIDADE_INVALIDA";
    public static final String POSICAO_PRECO_INVALIDO = "POSICAO_PRECO_INVALIDO";
    public static final String POSICAO_TOTAL_LIQUIDO_INVALIDO = "POSICAO_TOTAL_LIQUIDO_INVALIDO";

    public static final String SNAPSHOT_SEM_HISTORICO = "SNAPSHOT_SEM_HISTORICO";
    public static final String PROJECAO_TAXA_INVALIDA = "PROJECAO_TAXA_INVALIDA";
    public static final String PROJECAO_MESES_INVALIDOS = "PROJECAO_MESES_INVALIDOS";

    public static final String RECURSO_NAO_ENCONTRADO = "RECURSO_NAO_ENCONTRADO";
    public static final String NAO_AUTORIZADO = "NAO_AUTORIZADO";
    public static final String ACESSO_NEGADO = "ACESSO_NEGADO";
    public static final String ERRO_INTERNO = "ERRO_INTERNO";
    public static final String ERRO_VALIDACAO = "ERRO_VALIDACAO";
}
