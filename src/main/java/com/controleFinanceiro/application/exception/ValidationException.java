package com.controleFinanceiro.application.exception;

public class ValidationException extends ApplicationException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    public ValidationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
