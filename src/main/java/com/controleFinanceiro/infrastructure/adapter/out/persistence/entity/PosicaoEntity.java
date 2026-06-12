package com.controleFinanceiro.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "posicoes")
@Getter
@Setter
@NoArgsConstructor
public class PosicaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private SnapshotEntity snapshot;

    @Column(name = "ativo_id", nullable = false)
    private UUID ativoId;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal quantidade;

    @Column(name = "preco_unit", nullable = false, precision = 18, scale = 4)
    private BigDecimal precoUnit;

    @Column(name = "total_bruto", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalBruto;

    @Column(name = "total_liq", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalLiq;

    @Column(length = 50)
    private String taxa;
}
