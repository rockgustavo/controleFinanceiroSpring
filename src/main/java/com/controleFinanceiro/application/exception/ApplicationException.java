package com.controleFinanceiro.application.exception;

public class ApplicationException extends RuntimeException {

    private final String errorCode;

    public ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
