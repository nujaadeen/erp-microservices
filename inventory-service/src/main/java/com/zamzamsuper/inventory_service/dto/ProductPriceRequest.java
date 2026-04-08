package com.zamzamsuper.inventory_service.dto;

import java.math.BigDecimal;

import com.zamzamsuper.inventory_service.enums.PriceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceRequest {
    private Long batchId;
    private PriceType priceType;
    private Boolean active;
    private Integer minQuantity;
    private BigDecimal price;
    private BigDecimal minPrice;
}