package com.controleFinanceiro.application.exception;

import com.controleFinanceiro.domain.shared.ErrorCodes;

public class ValidationException extends ApplicationException {

    public ValidationException(String messageKey, String errorCode, Object... args) {
        super(messageKey, errorCode, args);
    }

    public ValidationException(String messageKey) {
        super(messageKey, ErrorCodes.VALIDATION_ERROR);
    }
}
