package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.SupplierReturn;

public interface SupplierReturnRepository extends JpaRepository<SupplierReturn, Long> {
}