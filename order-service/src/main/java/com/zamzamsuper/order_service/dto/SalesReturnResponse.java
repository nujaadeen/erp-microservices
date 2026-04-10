package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zamzamsuper.order_service.enums.RefundMethod;
import com.zamzamsuper.order_service.enums.ReturnStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnResponse {
    private Long id;
    private Long orderId;
    private String invoiceNumber;
    private LocalDateTime returnDate;
    private RefundMethod refundMethod;
    private BigDecimal totalRefundAmount;
    private ReturnStatus returnStatus;
    private String approvedById;
    private LocalDateTime createdAt;
    private List<SalesReturnItemResponse> items;
}
