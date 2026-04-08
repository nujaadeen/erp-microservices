package com.zamzamsuper.product_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.zamzamsuper.product_service.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String sku;
    private String category;
    private BigDecimal taxRate;
    private Unit baseUnit;
    private List<BarcodeResponse> barcodes;
}