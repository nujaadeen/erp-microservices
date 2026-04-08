package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.StockAdjustmentRequest;
import com.zamzamsuper.inventory_service.dto.StockAdjustmentResponse;
import com.zamzamsuper.inventory_service.model.Batch;
import com.zamzamsuper.inventory_service.model.StockAdjustment;
import com.zamzamsuper.inventory_service.repository.BatchRepository;
import com.zamzamsuper.inventory_service.repository.StockAdjustmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockAdjustmentService {

    private final StockAdjustmentRepository adjustmentRepository;
    private final BatchRepository batchRepository;

    public StockAdjustmentResponse createAdjustment(StockAdjustmentRequest request) {
        StockAdjustment adjustment = mapToEntity(request);
        StockAdjustment savedAdjustment = adjustmentRepository.save(adjustment);
        return mapToResponse(savedAdjustment);
    }

    public List<StockAdjustmentResponse> getAllAdjustments() {
        return adjustmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StockAdjustmentResponse getAdjustmentById(Long id) {
        StockAdjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adjustment not found with id " + id));
        return mapToResponse(adjustment);
    }

    public StockAdjustmentResponse updateAdjustment(Long id, StockAdjustmentRequest request) {
        StockAdjustment existing = adjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adjustment not found with id " + id));

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        existing.setBatch(batch);
        existing.setStaffId(request.getStaffId());
        existing.setAdjustmentType(request.getAdjustmentType());
        existing.setQuantity(request.getQuantity());
        existing.setReason(request.getReason());

        StockAdjustment updated = adjustmentRepository.save(existing);
        return mapToResponse(updated);
    }

    public void deleteAdjustment(Long id) {
        if (!adjustmentRepository.existsById(id)) {
            throw new RuntimeException("Adjustment not found with id " + id);
        }
        adjustmentRepository.deleteById(id);
    }

    private StockAdjustmentResponse mapToResponse(StockAdjustment adjustment) {
        return StockAdjustmentResponse.builder()
                .id(adjustment.getId())
                .batchId(adjustment.getBatch().getId())
                .batchNumber(adjustment.getBatch().getBatchNumber())
                .staffId(adjustment.getStaffId())
                .adjustmentType(adjustment.getAdjustmentType())
                .quantity(adjustment.getQuantity())
                .reason(adjustment.getReason())
                .createdAt(adjustment.getCreatedAt())
                .updatedAt(adjustment.getUpdatedAt())
                .build();
    }

    private StockAdjustment mapToEntity(StockAdjustmentRequest request) {
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        return StockAdjustment.builder()
                .batch(batch)
                .staffId(request.getStaffId())
                .adjustmentType(request.getAdjustmentType())
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .build();
    }
}