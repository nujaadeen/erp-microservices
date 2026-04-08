package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponse {
    private Long id;
    private Long stockId;
    private Long grnId;
    private LocalDate expiryDate;
    private String batchNumber;
    private BigDecimal costPrice;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}