package com.controleFinanceiro.application.usecase.command.ativo;

import com.controleFinanceiro.domain.event.AtivoAlteradoEvent;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.model.Ativo;
import com.controleFinanceiro.domain.port.in.command.CriarAtivoPort;
import com.controleFinanceiro.domain.port.out.command.AtivoRepositoryPort;
import com.controleFinanceiro.domain.port.out.messaging.EventPublisherPort;
import com.controleFinanceiro.domain.service.factory.AtivoValidatorFactory;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriarAtivoUseCase implements CriarAtivoPort {

    private final AtivoRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Ativo execute(CriarAtivoCommand command) {
        if (command.ticker() != null && repository.existsByTickerAndNotArchived(command.ticker())) {
            throw new ConflictException(MessageKeys.ATIVO_TICKER_EXISTENTE, ErrorCodes.TICKER_JA_CADASTRADO, command.ticker());
        }
        var ativo = Ativo.create(command.nome(), command.tipo(), command.ticker(), command.observacoes());
        AtivoValidatorFactory.forType(ativo.getTipo()).validate(ativo);
        var salvo = repository.save(ativo);
        eventPublisher.publish(AtivoAlteradoEvent.created(salvo.getId()));
        return salvo;
    }
}
