package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
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
public class OrderRequest {
    private String customerId;
    private String staffId;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalItemDiscountAmount;
    private BigDecimal orderDiscountAmount;
    private BigDecimal grandTotal;
    private BigDecimal paidAmount;
    private BigDecimal creditAmount;
    private BigDecimal balanceAmount;
    private OrderStatus status;
    private OrderType orderType;
    
    private List<OrderItemRequest> items;
    private List<OrderPromotionRequest> orderPromotions;
    private List<PaymentRequest> payments;
}