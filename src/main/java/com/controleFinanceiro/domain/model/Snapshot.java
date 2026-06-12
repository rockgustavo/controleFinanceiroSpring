package com.controleFinanceiro.domain.model;

import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Snapshot {

    private UUID id;
    private LocalDate data;
    private String observacoes;
    private List<Posicao> posicoes;
    private Instant criadoEm;

    private Snapshot() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDate data;
        private String observacoes;
        private final List<Posicao> posicoes = new ArrayList<>();

        public Builder data(LocalDate data) { this.data = data; return this; }
        public Builder observacoes(String observacoes) { this.observacoes = observacoes; return this; }
        public Builder posicao(Posicao p) { this.posicoes.add(p); return this; }

        public Snapshot build() {
            Objects.requireNonNull(data, "Data do snapshot é obrigatória");
            if (posicoes.isEmpty())
                throw new DomainException(MessageKeys.SNAPSHOT_SEM_POSICOES, ErrorCodes.SNAPSHOT_SEM_POSICOES);
            var s = new Snapshot();
            s.data = this.data;
            s.observacoes = this.observacoes;
            s.posicoes = List.copyOf(this.posicoes);
            s.criadoEm = Instant.now();
            return s;
        }
    }

    public static Snapshot reconstitute(UUID id, LocalDate data, String observacoes,
                                         List<Posicao> posicoes, Instant criadoEm) {
        var s = new Snapshot();
        s.id = id;
        s.data = data;
        s.observacoes = observacoes;
        s.posicoes = List.copyOf(posicoes);
        s.criadoEm = criadoEm;
        return s;
    }

    public UUID getId() { return id; }
    public LocalDate getData() { return data; }
    public String getObservacoes() { return observacoes; }
    public List<Posicao> getPosicoes() { return posicoes; }
    public Instant getCriadoEm() { return criadoEm; }
}
