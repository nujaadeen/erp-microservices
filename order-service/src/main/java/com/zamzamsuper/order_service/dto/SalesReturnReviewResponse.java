package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.zamzamsuper.order_service.enums.ReturnStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnReviewResponse {
    private Long orderId;
    private String invoiceNumber;
    private BigDecimal totalRefundAmount;
    private ReturnStatus returnStatus;
    private List<SalesReturnItemReviewResponse> items;
}
