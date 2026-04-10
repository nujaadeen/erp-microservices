package com.zamzamsuper.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.order_service.model.SalesReturn;

public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {
}
