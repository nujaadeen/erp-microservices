package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}