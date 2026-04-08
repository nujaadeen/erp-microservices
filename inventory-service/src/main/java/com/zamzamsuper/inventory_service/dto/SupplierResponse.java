package com.zamzamsuper.inventory_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {
    private Long id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}