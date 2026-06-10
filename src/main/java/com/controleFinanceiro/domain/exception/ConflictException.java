package com.controleFinanceiro.domain.exception;

public class ConflictException extends DomainException {

    public ConflictException(String message, String errorCode) {
        super(message, errorCode);
    }
}
