package com.controleFinanceiro.domain.shared;

public final class ErrorCodes {

    private ErrorCodes() {}

    public static final String ASSET_NOT_FOUND = "ASSET_NOT_FOUND";
    public static final String TICKER_REQUIRED = "TICKER_REQUIRED";
    public static final String TICKER_ALREADY_EXISTS = "TICKER_ALREADY_EXISTS";
    public static final String ASSET_NAME_REQUIRED = "ASSET_NAME_REQUIRED";
    public static final String ASSET_TYPE_REQUIRED = "ASSET_TYPE_REQUIRED";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
}
