package com.zamzamsuper.order_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zamzamsuper.order_service.dto.SalesReturnRequest;
import com.zamzamsuper.order_service.dto.SalesReturnResponse;
import com.zamzamsuper.order_service.dto.SalesReturnReviewRequest;
import com.zamzamsuper.order_service.dto.SalesReturnReviewResponse;
import com.zamzamsuper.order_service.service.SalesReturnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales_return")
@RequiredArgsConstructor
public class SalesReturnController {
    private final SalesReturnService salesReturnService;

    @PostMapping
    public ResponseEntity<SalesReturnResponse> create(@RequestBody SalesReturnRequest request) {
        return ResponseEntity.ok(salesReturnService.createReturn(request));
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<SalesReturnResponse> update(@PathVariable Long id, @RequestBody SalesReturnRequest request) {
    //     return ResponseEntity.ok(salesReturnService.updateReturn(id, request));
    // }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalesReturnResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.getSalesReturnById(id));
    }

    @GetMapping
    public ResponseEntity<List<SalesReturnResponse>> getAllSalesReturns() {
        return ResponseEntity.ok(salesReturnService.getAllSalesReturns());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salesReturnService.deleteSalesReturn(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate_preview")
    public ResponseEntity<SalesReturnReviewResponse> calculateReview(
            @RequestBody SalesReturnReviewRequest request) {
        
        SalesReturnReviewResponse response = salesReturnService.calculateRefundPreview(request);
        return ResponseEntity.ok(response);
    }
}
