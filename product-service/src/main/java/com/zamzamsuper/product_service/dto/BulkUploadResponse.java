package com.zamzamsuper.product_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadResponse {
    private int successCount;
    private int failureCount;
    private List<BulkUploadError> errors;
    private String reportDownloadUrl;
}
