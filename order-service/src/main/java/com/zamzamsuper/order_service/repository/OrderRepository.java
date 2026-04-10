package com.zamzamsuper.order_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByInvoiceNumber(String invoiceNumber);
}