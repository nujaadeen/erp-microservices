package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
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
public class SalesReturnRequest {
    private Long orderId;
    private String invoiceNumber;
    private RefundMethod refundMethod;
    private BigDecimal totalRefundAmount;
    private ReturnStatus returnStatus;
    private String approvedById;
    private List<SalesReturnItemRequest> items;
}
