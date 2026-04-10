package com.zamzamsuper.order_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zamzamsuper.order_service.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.invoiceNumber = :invoiceNumber AND oi.batchNumber = :batchNumber")
    Optional<OrderItem> findByInvoiceAndBatch(String invoiceNumber, String batchNumber);
}
