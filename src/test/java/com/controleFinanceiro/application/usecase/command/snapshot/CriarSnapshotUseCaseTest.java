package com.controleFinanceiro.application.usecase.command.snapshot;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.event.SnapshotCriadoEvent;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.Posicao;
import com.controleFinanceiro.domain.model.Snapshot;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.command.SnapshotRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarSnapshotUseCaseTest {

    @Mock SnapshotRepositoryPort snapshotRepository;
    @Mock AtivoRepositoryPort ativoRepository;
    @Mock EventPublisherPort eventPublisher;
    @InjectMocks CriarSnapshotUseCase useCase;

    private CriarSnapshotCommand commandValido(UUID ativoId) {
        var posicao = new CriarSnapshotCommand.PosicaoInput(
                ativoId, new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("480"), null);
        return new CriarSnapshotCommand(LocalDate.of(2024, 6, 1), "obs", List.of(posicao));
    }

    @Test
    void execute_cria_snapshot_persiste_e_publica_evento() {
        var ativoId = UUID.randomUUID();
        var ativo = Ativo.reconstitute(ativoId, "Tesouro", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
        var command = commandValido(ativoId);
        var posicao = Posicao.of(ativoId, new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("480"), null);
        var snapshotSalvo = Snapshot.reconstitute(UUID.randomUUID(), command.data(),
                command.observacoes(), List.of(posicao), Instant.now());

        when(snapshotRepository.existsByData(command.data())).thenReturn(false);
        when(ativoRepository.findById(ativoId)).thenReturn(Optional.of(ativo));
        when(snapshotRepository.save(any())).thenReturn(snapshotSalvo);

        var resultado = useCase.execute(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        verify(snapshotRepository).save(any());
        verify(eventPublisher).publish(any(SnapshotCriadoEvent.class));
    }

    @Test
    void execute_falha_no_publisher_nao_interrompe_fluxo() {
        var ativoId = UUID.randomUUID();
        var ativo = Ativo.reconstitute(ativoId, "Tesouro", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
        var command = commandValido(ativoId);
        var posicao = Posicao.of(ativoId, new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("480"), null);
        var snapshotSalvo = Snapshot.reconstitute(UUID.randomUUID(), command.data(),
                command.observacoes(), List.of(posicao), Instant.now());

        when(snapshotRepository.existsByData(command.data())).thenReturn(false);
        when(ativoRepository.findById(ativoId)).thenReturn(Optional.of(ativo));
        when(snapshotRepository.save(any())).thenReturn(snapshotSalvo);
        doThrow(new RuntimeException("RabbitMQ offline")).when(eventPublisher).publish(any(SnapshotCriadoEvent.class));

        assertThatCode(() -> useCase.execute(command)).doesNotThrowAnyException();
        verify(snapshotRepository).save(any());
    }
}
