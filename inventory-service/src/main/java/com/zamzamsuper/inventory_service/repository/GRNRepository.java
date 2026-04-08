package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.GRN;

public interface GRNRepository extends JpaRepository<GRN, Long> {
}