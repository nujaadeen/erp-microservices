package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierReturnItemRequest {
    private Long supplierReturnId;
    private Long batchId;
    private Integer qtyReturned;
    private BigDecimal unitCostRefunded;
    private String reason;
}