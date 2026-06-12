package com.controleFinanceiro.infrastructure.adapter.out.persistence.repository;

import com.controleFinanceiro.infrastructure.adapter.out.persistence.entity.SnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface SnapshotJpaRepository extends JpaRepository<SnapshotEntity, UUID> {
    boolean existsByData(LocalDate data);
}
