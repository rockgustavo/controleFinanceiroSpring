package com.controleFinanceiro.domain.port.in.query;

import com.controleFinanceiro.application.dto.response.AssetResponse;

import java.util.List;

public interface ListAssetsPort {
    List<AssetResponse> execute();
}
