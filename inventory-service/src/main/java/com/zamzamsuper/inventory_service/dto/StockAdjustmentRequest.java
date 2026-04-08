package com.zamzamsuper.inventory_service.dto;

import com.zamzamsuper.inventory_service.enums.AdjustmentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentRequest {
    private Long batchId;
    private String staffId;
    private AdjustmentType adjustmentType;
    private Integer quantity;
    private String reason;
}