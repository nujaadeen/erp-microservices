package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private String batchNumber;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private List<OrderItemPromotionRequest> itemPromotions;
}
