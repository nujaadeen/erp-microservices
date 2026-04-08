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

import com.zamzamsuper.inventory_service.dto.SupplierReturnItemRequest;
import com.zamzamsuper.inventory_service.dto.SupplierReturnItemResponse;
import com.zamzamsuper.inventory_service.service.SupplierReturnItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/supplier_return_item")
public class SupplierReturnItemController {

    private final SupplierReturnItemService itemService;

    @PostMapping
    public ResponseEntity<SupplierReturnItemResponse> createItem(@RequestBody SupplierReturnItemRequest request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @GetMapping
    public ResponseEntity<List<SupplierReturnItemResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierReturnItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierReturnItemResponse> updateItem(
            @PathVariable Long id, 
            @RequestBody SupplierReturnItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}