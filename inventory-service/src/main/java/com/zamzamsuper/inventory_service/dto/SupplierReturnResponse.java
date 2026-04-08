package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.zamzamsuper.inventory_service.enums.ReturnStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierReturnResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private Long originalGrnId;
    private LocalDate returnDate;
    private BigDecimal totalRefundValue;
    private ReturnStatus returnStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}