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

import com.zamzamsuper.inventory_service.dto.BatchRequest;
import com.zamzamsuper.inventory_service.dto.BatchResponse;
import com.zamzamsuper.inventory_service.service.BatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/batch")
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(@RequestBody BatchRequest request) {
        return ResponseEntity.ok(batchService.createBatch(request));
    }

    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchResponse> updateBatch(
            @PathVariable Long id,
            @RequestBody BatchRequest request) {
        return ResponseEntity.ok(batchService.updateBatch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}