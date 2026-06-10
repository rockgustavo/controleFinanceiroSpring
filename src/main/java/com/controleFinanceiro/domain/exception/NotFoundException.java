package com.controleFinanceiro.domain.exception;

public class NotFoundException extends DomainException {

    public NotFoundException(String messageKey, String errorCode, Object... args) {
        super(messageKey, errorCode, args);
    }
}
