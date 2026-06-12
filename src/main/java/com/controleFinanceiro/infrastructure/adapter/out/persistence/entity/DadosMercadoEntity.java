package com.controleFinanceiro.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dados_mercado")
@Getter
@Setter
@NoArgsConstructor
public class DadosMercadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false, unique = true)
    private SnapshotEntity snapshot;

    @Column(precision = 6, scale = 2)
    private BigDecimal selic;

    @Column(name = "usd_brl", precision = 8, scale = 4)
    private BigDecimal usdBrl;

    @Column(precision = 12, scale = 2)
    private BigDecimal ibovespa;

    @Column(precision = 10, scale = 4)
    private BigDecimal ivvb11;

    @Column(precision = 6, scale = 2)
    private BigDecimal ipca;

    @Column(name = "buscado_em", nullable = false, updatable = false)
    private Instant buscadoEm = Instant.now();
}
