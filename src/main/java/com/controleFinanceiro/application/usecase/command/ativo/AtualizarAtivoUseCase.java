package com.controleFinanceiro.application.usecase.command.ativo;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.port.in.command.AtualizarAtivoPort;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.domain.service.factory.AtivoValidatorFactory;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtualizarAtivoUseCase implements AtualizarAtivoPort {

    private final AtivoRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Ativo execute(UUID id, AtualizarAtivoCommand command) {
        var ativo = repository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException(MessageKeys.ATIVO_NAO_ENCONTRADO, ErrorCodes.ATIVO_NAO_ENCONTRADO, id));
        if (command.ticker() != null
                && !command.ticker().equals(ativo.getTicker())
                && repository.existsByTickerAndNotArchivedExcluding(command.ticker(), id)) {
            throw new ConflictException(MessageKeys.ATIVO_TICKER_EXISTENTE, ErrorCodes.TICKER_JA_CADASTRADO, command.ticker());
        }
        ativo.atualizar(command.nome(), command.ticker(), command.observacoes());
        AtivoValidatorFactory.forType(ativo.getTipo()).validate(ativo);
        var salvo = repository.save(ativo);
        eventPublisher.publish(AtivoAlteradoEvent.updated(salvo.getId()));
        return salvo;
    }
}
