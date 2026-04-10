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
public class SalesReturnItemRequest {
    private Long id; // Required for editing existing items
    private Long orderItemId;
    private String batchNumber;
    private Integer qtyReturned;
    private BigDecimal refundUnitPrice;
    private BigDecimal refundSubTotal;
    private Boolean manualPrice;
    private ItemCondition itemCondition;
    private String reason;
}
