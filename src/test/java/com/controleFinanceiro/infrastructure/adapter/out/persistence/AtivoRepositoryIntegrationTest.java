package com.controleFinanceiro.infrastructure.adapter.out.persistence;

import java.time.Instant;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.controleFinanceiro.domain.model.TipoAtivo;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.AtivoEntity;
import com.controleFinanceiro.infrastructure.adapter.out.persistence.repository.AtivoJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
@Testcontainers(disabledWithoutDocker = true)
class AtivoRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    AtivoJpaRepository repository;

    private AtivoEntity novoAtivo(String nome, TipoAtivo tipo, String ticker) {
        var entity = new AtivoEntity();
        entity.setNome(nome);
        entity.setTipo(tipo);
        entity.setTicker(ticker);
        entity.setCriadoEm(Instant.now());
        entity.setAtualizadoEm(Instant.now());
        return entity;
    }

    @Test
    void save_persiste_e_findById_recupera_ativo() {
        var entity = Objects.requireNonNull(novoAtivo("Tesouro Selic", TipoAtivo.RENDA_FIXA, null));

        var salvo = Objects.requireNonNull(repository.save(entity));

        assertThat(salvo.getId()).isNotNull();

        var encontrado = repository.findById(Objects.requireNonNull(salvo.getId()));
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Tesouro Selic");
        assertThat(encontrado.get().getTipo()).isEqualTo(TipoAtivo.RENDA_FIXA);
    }

    @Test
    void existsByTickerAndArquivadoEmIsNull_retorna_true_para_ticker_ativo() {
        repository.save(Objects.requireNonNull(novoAtivo("Ação Petro", TipoAtivo.RENDA_VARIAVEL, "PETR4")));

        assertThat(repository.existsByTickerAndArquivadoEmIsNull("PETR4")).isTrue();
        assertThat(repository.existsByTickerAndArquivadoEmIsNull("ELET3")).isFalse();
    }

    @Test
    void existsByTickerAndArquivadoEmIsNull_ignora_ativos_arquivados() {
        var entity = Objects.requireNonNull(novoAtivo("Ação Arquivada", TipoAtivo.RENDA_VARIAVEL, "ARCH3"));
        entity.setArquivadoEm(Instant.now());
        repository.save(entity);

        assertThat(repository.existsByTickerAndArquivadoEmIsNull("ARCH3")).isFalse();
    }

    @Test
    void findByIdAndArquivadoEmIsNull_retorna_empty_quando_ativo_arquivado() {
        var entity = Objects.requireNonNull(novoAtivo("Ativo Arquivado", TipoAtivo.RENDA_FIXA, null));
        entity.setArquivadoEm(Instant.now());
        var salvo = Objects.requireNonNull(repository.save(entity));

        assertThat(repository.findByIdAndArquivadoEmIsNull(Objects.requireNonNull(salvo.getId()))).isEmpty();
    }

    @Test
    void findByIdAndArquivadoEmIsNull_retorna_ativo_quando_nao_arquivado() {
        var entity = Objects.requireNonNull(novoAtivo("Ativo Ativo", TipoAtivo.RENDA_FIXA, null));
        var salvo = Objects.requireNonNull(repository.save(entity));

        assertThat(repository.findByIdAndArquivadoEmIsNull(Objects.requireNonNull(salvo.getId()))).isPresent();
    }

    @Test
    void existsByTickerAndArquivadoEmIsNullAndIdNot_exclui_o_proprio_ativo() {
        var entity = Objects.requireNonNull(novoAtivo("Ação X", TipoAtivo.RENDA_VARIAVEL, "XPTO3"));
        var salvo = Objects.requireNonNull(repository.save(entity));

        assertThat(repository.existsByTickerAndArquivadoEmIsNullAndIdNot(
                "XPTO3", Objects.requireNonNull(salvo.getId()))).isFalse();
    }

    @Test
    void existsByTickerAndArquivadoEmIsNullAndIdNot_detecta_conflito_com_outro_ativo() {
        var entity1 = Objects.requireNonNull(novoAtivo("Ação A", TipoAtivo.RENDA_VARIAVEL, "ACAO3"));
        var entity2 = Objects.requireNonNull(novoAtivo("Ação B", TipoAtivo.RENDA_VARIAVEL, "ACAO3"));
        repository.save(entity1);
        var salvo2 = Objects.requireNonNull(repository.save(entity2));

        assertThat(repository.existsByTickerAndArquivadoEmIsNullAndIdNot(
                "ACAO3", Objects.requireNonNull(salvo2.getId()))).isTrue();
    }
}
