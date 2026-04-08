package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.BatchRequest;
import com.zamzamsuper.inventory_service.dto.BatchResponse;
import com.zamzamsuper.inventory_service.model.Batch;
import com.zamzamsuper.inventory_service.model.GRN;
import com.zamzamsuper.inventory_service.model.Stock;
import com.zamzamsuper.inventory_service.repository.BatchRepository;
import com.zamzamsuper.inventory_service.repository.GRNRepository;
import com.zamzamsuper.inventory_service.repository.StockRepository; 

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;
    private final StockRepository stockRepository;
    private final GRNRepository grnRepository;

    public BatchResponse createBatch(BatchRequest request) {
        Batch batch = mapToEntity(request);
        Batch savedBatch = batchRepository.save(batch);
        return mapToResponse(savedBatch);
    }

    public List<BatchResponse> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BatchResponse getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id " + id));
        return mapToResponse(batch);
    }

    public BatchResponse updateBatch(Long id, BatchRequest request) {
        Batch existingBatch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id " + id));

        // Fetch new associations if they changed
        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        GRN grn = grnRepository.findById(request.getGrnId())
                .orElseThrow(() -> new RuntimeException("GRN not found"));

        existingBatch.setStock(stock);
        existingBatch.setGrn(grn);
        existingBatch.setBatchNumber(request.getBatchNumber());
        existingBatch.setExpiryDate(request.getExpiryDate());
        existingBatch.setCostPrice(request.getCostPrice());
        existingBatch.setQuantity(request.getQuantity());

        Batch saved = batchRepository.save(existingBatch);
        return mapToResponse(saved);
    }

    public void deleteBatch(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new RuntimeException("Batch not found with id " + id);
        }
        batchRepository.deleteById(id);
    }

    private BatchResponse mapToResponse(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .stockId(batch.getStock().getId())
                .grnId(batch.getGrn().getId())
                .batchNumber(batch.getBatchNumber())
                .expiryDate(batch.getExpiryDate())
                .costPrice(batch.getCostPrice())
                .quantity(batch.getQuantity())
                .createdAt(batch.getCreatedAt())
                .updatedAt(batch.getUpdatedAt())
                .build();
    }

    private Batch mapToEntity(BatchRequest request) {
        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        GRN grn = grnRepository.findById(request.getGrnId())
                .orElseThrow(() -> new RuntimeException("GRN not found"));

        return Batch.builder()
                .stock(stock)
                .grn(grn)
                .batchNumber(request.getBatchNumber())
                .expiryDate(request.getExpiryDate())
                .costPrice(request.getCostPrice())
                .quantity(request.getQuantity())
                .build();
    }
}