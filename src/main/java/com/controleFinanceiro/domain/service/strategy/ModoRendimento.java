package com.controleFinanceiro.domain.service.strategy;

public enum ModoRendimento {

    SIMPLES("simple"),
    CAGR("cagr");

    private final String codigo;

    ModoRendimento(String codigo) {
        this.codigo = codigo;
    }

    public String codigo() {
        return codigo;
    }

    public static ModoRendimento de(String valor) {
        return CAGR.codigo.equalsIgnoreCase(valor) ? CAGR : SIMPLES;
    }

    public ReturnCalculationStrategy novaEstrategia() {
        return switch (this) {
            case CAGR -> new CagrStrategy();
            case SIMPLES -> new SimpleReturnStrategy();
        };
    }
}
