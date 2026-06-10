package com.controleFinanceiro.domain.shared;

public final class MessageKeys {

    private MessageKeys() {}

    public static final String ASSET_NOT_FOUND = "error.asset.not_found";
    public static final String ASSET_TICKER_REQUIRED = "error.asset.ticker.required";
    public static final String ASSET_TICKER_EXISTS = "error.asset.ticker.exists";
    public static final String ASSET_NAME_REQUIRED = "error.asset.name.required";
    public static final String ASSET_TYPE_REQUIRED = "error.asset.type.required";

    public static final String SECURITY_UNAUTHORIZED = "error.security.unauthorized";
    public static final String SECURITY_FORBIDDEN = "error.security.forbidden";

    public static final String ERROR_INTERNAL = "error.internal";
    public static final String VALIDATION = "error.validation";
}
