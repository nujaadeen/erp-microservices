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

import com.zamzamsuper.inventory_service.dto.SupplierRequest;
import com.zamzamsuper.inventory_service.dto.SupplierResponse;
import com.zamzamsuper.inventory_service.service.SupplierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierRequest request) {
        return ResponseEntity.ok(supplierService.createSupplier(request));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id, 
            @RequestBody SupplierRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}