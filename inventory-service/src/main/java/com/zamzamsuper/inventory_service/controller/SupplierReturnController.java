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

import com.zamzamsuper.inventory_service.dto.SupplierReturnRequest;
import com.zamzamsuper.inventory_service.dto.SupplierReturnResponse;
import com.zamzamsuper.inventory_service.service.SupplierReturnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/supplier_return")
public class SupplierReturnController {

    private final SupplierReturnService supplierReturnService;

    @PostMapping
    public ResponseEntity<SupplierReturnResponse> createReturn(@RequestBody SupplierReturnRequest request) {
        return ResponseEntity.ok(supplierReturnService.createReturn(request));
    }

    @GetMapping
    public ResponseEntity<List<SupplierReturnResponse>> getAllReturns() {
        return ResponseEntity.ok(supplierReturnService.getAllReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierReturnResponse> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierReturnService.getReturnById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierReturnResponse> updateReturn(
            @PathVariable Long id, 
            @RequestBody SupplierReturnRequest request) {
        return ResponseEntity.ok(supplierReturnService.updateReturn(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        supplierReturnService.deleteReturn(id);
        return ResponseEntity.noContent().build();
    }
}