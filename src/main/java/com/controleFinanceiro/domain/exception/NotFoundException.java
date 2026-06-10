package com.controleFinanceiro.domain.exception;

public class NotFoundException extends DomainException {

    public NotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
