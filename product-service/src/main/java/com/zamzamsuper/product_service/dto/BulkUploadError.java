package com.zamzamsuper.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadError {
    private int rowNumber;
    private String name;
    private String error;
}
