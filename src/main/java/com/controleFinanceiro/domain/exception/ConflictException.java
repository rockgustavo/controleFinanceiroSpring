package com.controleFinanceiro.domain.exception;

public class ConflictException extends DomainException {

    public ConflictException(String messageKey, String errorCode, Object... args) {
        super(messageKey, errorCode, args);
    }
}
