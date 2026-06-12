package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.DadosMercadoEntity;

public interface DadosMercadoJpaRepository extends JpaRepository<DadosMercadoEntity, UUID> {}
