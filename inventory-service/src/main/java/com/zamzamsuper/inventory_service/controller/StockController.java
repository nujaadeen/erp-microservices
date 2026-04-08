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

import com.zamzamsuper.inventory_service.dto.StockRequest;
import com.zamzamsuper.inventory_service.dto.StockResponse;
import com.zamzamsuper.inventory_service.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/stock")
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.createStock(request));
    }

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable Long id, 
            @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.updateStock(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}