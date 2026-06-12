package com.controleFinanceiro.domain.shared;

public final class MessageKeys {

    private MessageKeys() {}

    public static final String ATIVO_NAO_ENCONTRADO = "erro.ativo.nao_encontrado";
    public static final String ATIVO_TICKER_OBRIGATORIO = "erro.ativo.ticker.obrigatorio";
    public static final String ATIVO_TICKER_EXISTENTE = "erro.ativo.ticker.existente";
    public static final String ATIVO_NOME_OBRIGATORIO = "erro.ativo.nome.obrigatorio";
    public static final String ATIVO_TIPO_OBRIGATORIO = "erro.ativo.tipo.obrigatorio";

    public static final String SNAPSHOT_NAO_ENCONTRADO = "erro.snapshot.nao_encontrado";
    public static final String SNAPSHOT_NENHUM_REGISTRADO = "erro.snapshot.nenhum_registrado";
    public static final String SNAPSHOT_DATA_EXISTENTE = "erro.snapshot.data_existente";
    public static final String SNAPSHOT_DATA_OBRIGATORIA = "erro.snapshot.data_obrigatoria";
    public static final String SNAPSHOT_SEM_POSICOES = "erro.snapshot.sem_posicoes";
    public static final String SNAPSHOT_ATIVO_ARQUIVADO = "erro.snapshot.ativo_arquivado";
    public static final String SNAPSHOT_POSICAO_DUPLICADA = "erro.snapshot.posicao_duplicada";

    public static final String POSICAO_ATIVO_OBRIGATORIO = "erro.posicao.ativo_obrigatorio";
    public static final String POSICAO_QUANTIDADE_INVALIDA = "erro.posicao.quantidade_invalida";
    public static final String POSICAO_PRECO_INVALIDO = "erro.posicao.preco_invalido";
    public static final String POSICAO_TOTAL_LIQUIDO_INVALIDO = "erro.posicao.total_liquido_invalido";

    public static final String SNAPSHOT_SEM_HISTORICO = "erro.snapshot.sem_historico";
    public static final String PROJECAO_TAXA_INVALIDA = "erro.projecao.taxa_invalida";
    public static final String PROJECAO_MESES_INVALIDOS = "erro.projecao.meses_invalidos";

    public static final String SEGURANCA_NAO_AUTORIZADO = "erro.seguranca.nao_autorizado";
    public static final String SEGURANCA_ACESSO_NEGADO = "erro.seguranca.acesso_negado";

    public static final String ERRO_INTERNO = "erro.interno";
    public static final String ERRO_VALIDACAO = "erro.validacao";

    public static final String SUCESSO_ATIVO_CRIADO = "sucesso.ativo.criado";
    public static final String SUCESSO_ATIVO_ATUALIZADO = "sucesso.ativo.atualizado";
    public static final String SUCESSO_SNAPSHOT_CRIADO = "sucesso.snapshot.criado";
}
