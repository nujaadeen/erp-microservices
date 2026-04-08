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

import com.zamzamsuper.inventory_service.dto.ProductPriceRequest;
import com.zamzamsuper.inventory_service.dto.ProductPriceResponse;
import com.zamzamsuper.inventory_service.service.ProductPriceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/product_price")
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    @PostMapping
    public ResponseEntity<ProductPriceResponse> createPrice(@RequestBody ProductPriceRequest request) {
        return ResponseEntity.ok(productPriceService.createPrice(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductPriceResponse>> getAllPrices() {
        return ResponseEntity.ok(productPriceService.getAllPrices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPriceResponse> getPriceById(@PathVariable Long id) {
        return ResponseEntity.ok(productPriceService.getPriceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPriceResponse> updatePrice(
            @PathVariable Long id, 
            @RequestBody ProductPriceRequest request) {
        return ResponseEntity.ok(productPriceService.updatePrice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        productPriceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}