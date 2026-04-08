package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.zamzamsuper.inventory_service.enums.ReturnStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierReturnRequest {
    private Long supplierId;
    private Long originalGrnId;
    private LocalDate returnDate;
    private BigDecimal totalRefundValue;
    private ReturnStatus returnStatus;
}