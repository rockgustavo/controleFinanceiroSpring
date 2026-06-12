package com.controleFinanceiro.application.usecase.query.snapshot;

import java.util.List;

import org.springframework.stereotype.Service;

import com.controleFinanceiro.application.dto.response.SnapshotResumoResponse;
import com.controleFinanceiro.domain.port.in.query.ListarSnapshotsPort;
import com.controleFinanceiro.domain.port.out.query.SnapshotQueryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarSnapshotsUseCase implements ListarSnapshotsPort {

    private final SnapshotQueryPort queryPort;

    @Override
    public List<SnapshotResumoResponse> execute() {
        return queryPort.findAll();
    }
}
