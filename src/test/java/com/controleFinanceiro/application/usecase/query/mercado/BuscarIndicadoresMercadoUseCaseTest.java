package com.controleFinanceiro.application.usecase.query.mercado;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.controleFinanceiro.domain.model.DadosMercado;
import com.controleFinanceiro.domain.port.out.external.BrapiClientPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarIndicadoresMercadoUseCaseTest {

    @Mock BrapiClientPort brapiClient;
    @InjectMocks BuscarIndicadoresMercadoUseCase useCase;

    @Test
    void execute_retorna_indicadores_quando_brapi_disponivel() {
        var dadosMercado = new DadosMercado(
                new BigDecimal("10.75"),
                new BigDecimal("5.12"),
                new BigDecimal("130000"),
                new BigDecimal("42.50"),
                new BigDecimal("4.83")
        );
        when(brapiClient.buscarDadosMercado()).thenReturn(Optional.of(dadosMercado));

        var resultado = useCase.execute();

        assertThat(resultado.selic()).isEqualByComparingTo(new BigDecimal("10.75"));
        assertThat(resultado.usdBrl()).isEqualByComparingTo(new BigDecimal("5.12"));
        assertThat(resultado.ibovespa()).isEqualByComparingTo(new BigDecimal("130000"));
        assertThat(resultado.ivvb11()).isEqualByComparingTo(new BigDecimal("42.50"));
        assertThat(resultado.ipca()).isEqualByComparingTo(new BigDecimal("4.83"));
    }

    @Test
    void execute_retorna_indicadores_nulos_quando_brapi_indisponivel() {
        when(brapiClient.buscarDadosMercado()).thenReturn(Optional.empty());

        var resultado = useCase.execute();

        assertThat(resultado.selic()).isNull();
        assertThat(resultado.usdBrl()).isNull();
        assertThat(resultado.ibovespa()).isNull();
        assertThat(resultado.ivvb11()).isNull();
        assertThat(resultado.ipca()).isNull();
    }
}
