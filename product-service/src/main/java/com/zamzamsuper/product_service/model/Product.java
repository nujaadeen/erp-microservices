package com.zamzamsuper.product_service.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zamzamsuper.product_service.enums.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
@CompoundIndexes({
    @CompoundIndex(name = "barcode_idx", def = "{'barcodes.code': 1}")
})
public class Product {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String sku;
    private String category;
    private BigDecimal taxRate;

    @Indexed // helps but not perfect for array search, see note below
    private List<Barcode> barcodes;
    private Unit baseUnit;
}
