package com.controleFinanceiro.application.usecase.command.snapshot;

import com.controleFinanceiro.domain.event.SnapshotCriadoEvent;
import com.controleFinanceiro.domain.model.Posicao;
import com.controleFinanceiro.domain.model.Snapshot;
import com.controleFinanceiro.domain.port.in.command.CriarSnapshotPort;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.command.SnapshotRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CriarSnapshotUseCase implements CriarSnapshotPort {

    private final SnapshotRepositoryPort snapshotRepository;
    private final AtivoRepositoryPort ativoRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Snapshot execute(CriarSnapshotCommand command) {
        var cadeia = new DataUnicaHandler(snapshotRepository);
        cadeia.entao(new AtivosAtivosHandler(ativoRepository))
              .entao(new SemPosicoeDuplicadaHandler());
        cadeia.handle(command);

        var builder = Snapshot.builder()
                .data(command.data())
                .observacoes(command.observacoes());

        command.posicoes().forEach(p ->
                builder.posicao(Posicao.of(p.ativoId(), p.quantidade(), p.precoUnit(), p.totalLiq(), p.taxa()))
        );

        var snapshot = snapshotRepository.save(builder.build());

        try {
            eventPublisher.publish(SnapshotCriadoEvent.of(snapshot.getId(), snapshot.getData()));
        } catch (Exception e) {
            log.warn("Falha ao publicar evento de snapshot criado: {}", e.getMessage());
        }

        return snapshot;
    }
}
