package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.zamzamsuper.inventory_service.enums.PriceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceResponse {
    private Long id;
    private Long batchId;
    private String batchNumber; // Helpful for context
    private PriceType priceType;
    private Boolean active;
    private Integer minQuantity;
    private BigDecimal price;
    private BigDecimal minPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}