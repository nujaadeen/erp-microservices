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
public class SalesReturnItemReviewResponse {
    private Long orderItemId; // To link back to the original order item for UI display
    private String batchNumber;
    private Integer qtyReturned;
    private BigDecimal refundUnitPrice; // The BE-calculated or validated price
    private BigDecimal refundSubTotal; // (refundUnitPrice * qtyReturned)
    private Boolean manualPrice; // Flag to show UI if this was a manual override
    private ItemCondition itemCondition;
    private String reason;
}
