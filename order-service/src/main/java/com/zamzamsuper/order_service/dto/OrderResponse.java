package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zamzamsuper.order_service.enums.OrderStatus;
import com.zamzamsuper.order_service.enums.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerId;
    private String staffId;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private OrderStatus status;
    private OrderType orderType;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private List<OrderPromotionResponse> orderPromotions;
}