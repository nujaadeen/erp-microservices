package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;

import com.zamzamsuper.order_service.enums.PaymentMethod;
import com.zamzamsuper.order_service.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private PaymentMethod paymentMethod;
    private BigDecimal amountPaid;
    private PaymentStatus paymentStatus;
    private String transactionReference;
}
