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

import com.zamzamsuper.inventory_service.dto.GRNRequest;
import com.zamzamsuper.inventory_service.dto.GRNResponse;
import com.zamzamsuper.inventory_service.service.GRNService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/grn")
public class GRNController {

    private final GRNService grnService;

    @PostMapping
    public ResponseEntity<GRNResponse> createGRN(@RequestBody GRNRequest request) {
        return ResponseEntity.ok(grnService.createGRN(request));
    }

    @GetMapping
    public ResponseEntity<List<GRNResponse>> getAllGRNs() {
        return ResponseEntity.ok(grnService.getAllGRNs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GRNResponse> getGRNById(@PathVariable Long id) {
        return ResponseEntity.ok(grnService.getGRNById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GRNResponse> updateGRN(
            @PathVariable Long id, 
            @RequestBody GRNRequest request) {
        return ResponseEntity.ok(grnService.updateGRN(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGRN(@PathVariable Long id) {
        grnService.deleteGRN(id);
        return ResponseEntity.noContent().build();
    }
}