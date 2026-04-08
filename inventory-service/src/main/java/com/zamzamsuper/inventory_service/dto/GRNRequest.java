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
public class GRNRequest {
    private Long supplierId;
    private String invoiceNum;
    private BigDecimal totalAmount;
}