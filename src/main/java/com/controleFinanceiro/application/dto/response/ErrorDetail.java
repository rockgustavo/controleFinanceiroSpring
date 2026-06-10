package com.controleFinanceiro.application.dto.response;

import java.time.Instant;

public record ErrorDetail(
        String code,
        String message,
        int statusCode,
        String path,
        String timestamp
) {
    public static ErrorDetail of(String code, String message, int statusCode, String path) {
        return new ErrorDetail(code, message, statusCode, path, Instant.now().toString());
    }
}
