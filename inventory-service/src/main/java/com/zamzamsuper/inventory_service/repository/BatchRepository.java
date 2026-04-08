package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {
}