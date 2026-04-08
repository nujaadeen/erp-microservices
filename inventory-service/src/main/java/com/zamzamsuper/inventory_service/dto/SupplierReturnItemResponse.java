package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierReturnItemResponse {
    private Long id;
    private Long supplierReturnId;
    private Long batchId;
    private String batchNumber; // Helpful for context
    private Integer qtyReturned;
    private BigDecimal unitCostRefunded;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}