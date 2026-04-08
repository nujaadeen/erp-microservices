package com.zamzamsuper.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {
    private String productSku;
    private Long locationId;
    private Integer quantityOnHand;
    private Integer reorderLevel;
}