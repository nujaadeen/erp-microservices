package com.zamzamsuper.inventory_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zamzamsuper.inventory_service.dto.StockAdjustmentRequest;
import com.zamzamsuper.inventory_service.dto.StockAdjustmentResponse;
import com.zamzamsuper.inventory_service.service.StockAdjustmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/stock_adjustment")
public class StockAdjustmentController {

    private final StockAdjustmentService adjustmentService;

    @PostMapping
    public ResponseEntity<StockAdjustmentResponse> createAdjustment(@RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(adjustmentService.createAdjustment(request));
    }

    @GetMapping
    public ResponseEntity<List<StockAdjustmentResponse>> getAllAdjustments() {
        return ResponseEntity.ok(adjustmentService.getAllAdjustments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockAdjustmentResponse> getAdjustmentById(@PathVariable Long id) {
        return ResponseEntity.ok(adjustmentService.getAdjustmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockAdjustmentResponse> updateAdjustment(
            @PathVariable Long id, 
            @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(adjustmentService.updateAdjustment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdjustment(@PathVariable Long id) {
        adjustmentService.deleteAdjustment(id);
        return ResponseEntity.noContent().build();
    }
}