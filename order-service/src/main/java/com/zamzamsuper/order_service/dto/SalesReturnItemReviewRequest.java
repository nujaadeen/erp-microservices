package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;

import com.zamzamsuper.order_service.enums.ItemCondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnItemReviewRequest {
    private String batchNumber;
    private Integer qtyReturned;
    private BigDecimal refundUnitPrice; // Null if BE should calculate; provided if Manual
    private Boolean manualPrice; // Flag to show UI if this was a manual override
    private ItemCondition itemCondition;
    private String reason;
}
