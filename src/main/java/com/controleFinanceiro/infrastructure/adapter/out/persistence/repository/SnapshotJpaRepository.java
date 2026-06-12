package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.SnapshotEntity;

public interface SnapshotJpaRepository extends JpaRepository<SnapshotEntity, UUID> {
    boolean existsByData(LocalDate data);
}
