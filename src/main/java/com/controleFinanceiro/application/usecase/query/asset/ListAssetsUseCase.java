package com.controleFinanceiro.application.usecase.query.asset;

import com.controleFinanceiro.application.dto.response.AssetResponse;
import com.controleFinanceiro.domain.port.in.query.ListAssetsPort;
import com.controleFinanceiro.domain.port.out.query.AssetQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAssetsUseCase implements ListAssetsPort {

    private final AssetQueryPort queryPort;

    @Override
    public List<AssetResponse> execute() {
        return queryPort.findAllActive();
    }
}
