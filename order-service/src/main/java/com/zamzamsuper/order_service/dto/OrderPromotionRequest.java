package com.zamzamsuper.order_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPromotionRequest {
    private String promotionRuleCode;
    private BigDecimal discountAmount;
    private Boolean override;
    private String metadata;
}
