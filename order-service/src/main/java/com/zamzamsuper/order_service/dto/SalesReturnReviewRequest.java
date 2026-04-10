package com.zamzamsuper.order_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnReviewRequest {
    private String invoiceNumber; 
    private List<SalesReturnItemReviewRequest> items;    
}
