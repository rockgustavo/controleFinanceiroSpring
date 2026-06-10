package com.controleFinanceiro.domain.exception;

public class DomainException extends RuntimeException {

    private final String errorCode;
    private final Object[] args;

    public DomainException(String messageKey, String errorCode, Object... args) {
        super(messageKey);
        this.errorCode = errorCode;
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
