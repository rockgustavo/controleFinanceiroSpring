package com.controleFinanceiro.application.usecase.command.ativo;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArquivarAtivoUseCaseTest {

    @Mock AtivoRepositoryPort repository;
    @InjectMocks ArquivarAtivoUseCase useCase;

    @Test
    void execute_arquiva_ativo_e_salva() {
        var id = UUID.randomUUID();
        var ativo = Ativo.reconstitute(id, "Ativo", TipoAtivo.RENDA_FIXA,
                null, null, Instant.now(), Instant.now(), null);
        when(repository.findActiveById(id)).thenReturn(Optional.of(ativo));
        when(repository.save(any())).thenReturn(ativo);

        useCase.execute(id);

        assertThat(ativo.estaArquivado()).isTrue();
        verify(repository).save(ativo);
    }

    @Test
    void execute_ativo_nao_encontrado_lanca_NotFoundException() {
        var id = UUID.randomUUID();
        when(repository.findActiveById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);

        verify(repository, never()).save(any());
    }
}
