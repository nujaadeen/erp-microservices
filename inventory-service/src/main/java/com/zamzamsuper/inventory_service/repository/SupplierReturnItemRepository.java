package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.SupplierReturnItem;

public interface SupplierReturnItemRepository extends JpaRepository<SupplierReturnItem, Long> {
}