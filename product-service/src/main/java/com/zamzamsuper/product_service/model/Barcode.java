package com.zamzamsuper.product_service.model;

import com.zamzamsuper.product_service.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Barcode {
    private String code;
    private Unit unit;
    private int conversionFactor;
}
