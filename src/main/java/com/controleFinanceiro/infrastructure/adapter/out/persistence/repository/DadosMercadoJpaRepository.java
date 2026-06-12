package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.DadosMercadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DadosMercadoJpaRepository extends JpaRepository<DadosMercadoEntity, UUID> {}
