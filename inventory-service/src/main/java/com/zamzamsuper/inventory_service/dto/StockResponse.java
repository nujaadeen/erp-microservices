package com.zamzamsuper.inventory_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private Long id;
    private String productSku;
    private Long locationId;
    private String locationName; // Helpful for the UI
    private Integer quantityOnHand;
    private Integer reorderLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}