package com.controleFinanceiro.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Posicao {

    private UUID ativoId;
    private BigDecimal quantidade;
    private BigDecimal precoUnit;
    private BigDecimal totalBruto;
    private BigDecimal totalLiq;
    private String taxa;

    private Posicao() {}

    public static Posicao of(UUID ativoId, BigDecimal quantidade, BigDecimal precoUnit,
                              BigDecimal totalLiq, String taxa) {
        var posicao = new Posicao();
        posicao.ativoId = ativoId;
        posicao.quantidade = quantidade;
        posicao.precoUnit = precoUnit;
        posicao.totalBruto = quantidade.multiply(precoUnit);
        posicao.totalLiq = totalLiq;
        posicao.taxa = taxa;
        return posicao;
    }

    public static Posicao reconstitute(UUID ativoId, BigDecimal quantidade, BigDecimal precoUnit,
                                        BigDecimal totalBruto, BigDecimal totalLiq, String taxa) {
        var posicao = new Posicao();
        posicao.ativoId = ativoId;
        posicao.quantidade = quantidade;
        posicao.precoUnit = precoUnit;
        posicao.totalBruto = totalBruto;
        posicao.totalLiq = totalLiq;
        posicao.taxa = taxa;
        return posicao;
    }

    public UUID getAtivoId() { return ativoId; }
    public BigDecimal getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnit() { return precoUnit; }
    public BigDecimal getTotalBruto() { return totalBruto; }
    public BigDecimal getTotalLiq() { return totalLiq; }
    public String getTaxa() { return taxa; }
}
