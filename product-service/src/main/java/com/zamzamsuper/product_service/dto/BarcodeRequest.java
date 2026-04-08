package com.zamzamsuper.product_service.dto;

import com.zamzamsuper.product_service.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeRequest {
    private String code;
    private Unit unit;
    private int conversionFactor;
}