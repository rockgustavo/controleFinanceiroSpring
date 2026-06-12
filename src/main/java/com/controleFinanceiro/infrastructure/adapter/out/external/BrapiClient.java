package com.controleFinanceiro.infrastructure.adapter.out.external;

import com.controleFinanceiro.application.dto.response.TaxaTesourosResponse;
import com.controleFinanceiro.domain.model.DadosMercado;
import com.controleFinanceiro.domain.port.out.external.BrapiClientPort;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
public class BrapiClient implements BrapiClientPort {

    private final RestClient restClient;
    private final String token;

    public BrapiClient(@Qualifier("brapiRestClient") RestClient restClient,
                       @Value("${brapi.token:}") String token) {
        this.restClient = restClient;
        this.token = token;
    }

    @Override
    public Optional<DadosMercado> buscarDadosMercado() {
        if (!hasToken()) {
            return Optional.empty();
        }
        var selic = tryFetch(this::fetchSelic);
        var usdBrl = tryFetch(this::fetchUsdBrl);
        var ibovespa = tryFetch(() -> fetchQuote("IBOV"));
        var ivvb11 = tryFetch(() -> fetchQuote("IVVB11"));
        var ipca = tryFetch(this::fetchIpca);
        return Optional.of(new DadosMercado(selic, usdBrl, ibovespa, ivvb11, ipca));
    }

    @Override
    public List<TaxaTesourosResponse> buscarTaxasTesouro() {
        try {
            var uri = UriComponentsBuilder.fromPath("/api/v2/prime-rate")
                    .queryParam("historical", "false")
                    .queryParam("sortBy", "date")
                    .queryParam("sortOrder", "desc");
            if (hasToken()) uri.queryParam("token", token);
            var response = restClient.get().uri(uri.toUriString()).retrieve().body(PrimeRateResponse.class);
            if (response == null || response.items() == null) return List.of();
            return response.items().stream()
                    .map(i -> new TaxaTesourosResponse(i.type(), i.value(), i.date()))
                    .toList();
        } catch (Exception e) {
            log.warn("Falha ao buscar taxas de tesouro: {}", e.getMessage());
            return List.of();
        }
    }

    private BigDecimal fetchSelic() {
        var uri = UriComponentsBuilder.fromPath("/api/v2/prime-rate")
                .queryParam("historical", "false")
                .queryParam("sortBy", "date")
                .queryParam("sortOrder", "desc")
                .queryParam("token", token);
        var response = restClient.get().uri(uri.toUriString()).retrieve().body(PrimeRateResponse.class);
        if (response == null || response.items() == null || response.items().isEmpty()) return null;
        return response.items().get(0).value();
    }

    private BigDecimal fetchUsdBrl() {
        var uri = UriComponentsBuilder.fromPath("/api/v2/currency")
                .queryParam("currency", "USD-BRL")
                .queryParam("token", token);
        var response = restClient.get().uri(uri.toUriString()).retrieve().body(CurrencyResponse.class);
        if (response == null || response.currency() == null || response.currency().isEmpty()) return null;
        var bidPrice = response.currency().get(0).bidPrice();
        return bidPrice != null ? new BigDecimal(bidPrice) : null;
    }

    private BigDecimal fetchQuote(String symbol) {
        var uri = UriComponentsBuilder.fromPath("/api/quote/" + symbol)
                .queryParam("token", token);
        var response = restClient.get().uri(uri.toUriString()).retrieve().body(QuoteResponse.class);
        if (response == null || response.results() == null || response.results().isEmpty()) return null;
        return response.results().get(0).regularMarketPrice();
    }

    private BigDecimal fetchIpca() {
        var uri = UriComponentsBuilder.fromPath("/api/v2/inflation")
                .queryParam("historical", "false")
                .queryParam("sortBy", "date")
                .queryParam("sortOrder", "desc")
                .queryParam("token", token);
        var response = restClient.get().uri(uri.toUriString()).retrieve().body(InflationResponse.class);
        if (response == null || response.inflation() == null || response.inflation().isEmpty()) return null;
        return response.inflation().get(0).value();
    }

    private BigDecimal tryFetch(Supplier<BigDecimal> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.warn("Falha ao buscar indicador de mercado: {}", e.getMessage());
            return null;
        }
    }

    private boolean hasToken() {
        return token != null && !token.isBlank();
    }

    private record PrimeRateResponse(@JsonProperty("prime-rate") List<PrimeRateItem> items) {
        private record PrimeRateItem(String date, BigDecimal value, String type) {}
    }

    private record InflationResponse(List<InflationItem> inflation) {
        private record InflationItem(String date, BigDecimal value) {}
    }

    private record CurrencyResponse(List<CurrencyItem> currency) {
        private record CurrencyItem(String fromCurrency, String toCurrency, String bidPrice) {}
    }

    private record QuoteResponse(List<QuoteResult> results) {
        private record QuoteResult(String symbol, BigDecimal regularMarketPrice) {}
    }
}
