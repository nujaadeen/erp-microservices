package com.zamzamsuper.inventory_service.dto;

import java.time.LocalDateTime;

import com.zamzamsuper.inventory_service.enums.AdjustmentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentResponse {
    private Long id;
    private Long batchId;
    private String batchNumber; 
    private String staffId;
    private AdjustmentType adjustmentType;
    private Integer quantity;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}