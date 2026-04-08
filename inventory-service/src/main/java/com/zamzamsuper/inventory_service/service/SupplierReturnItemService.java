package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.SupplierReturnItemRequest;
import com.zamzamsuper.inventory_service.dto.SupplierReturnItemResponse;
import com.zamzamsuper.inventory_service.model.Batch;
import com.zamzamsuper.inventory_service.model.SupplierReturn;
import com.zamzamsuper.inventory_service.model.SupplierReturnItem;
import com.zamzamsuper.inventory_service.repository.BatchRepository;
import com.zamzamsuper.inventory_service.repository.SupplierReturnItemRepository;
import com.zamzamsuper.inventory_service.repository.SupplierReturnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierReturnItemService {

    private final SupplierReturnItemRepository itemRepository;
    private final SupplierReturnRepository returnRepository;
    private final BatchRepository batchRepository;

    public SupplierReturnItemResponse createItem(SupplierReturnItemRequest request) {
        SupplierReturnItem item = mapToEntity(request);
        SupplierReturnItem savedItem = itemRepository.save(item);
        return mapToResponse(savedItem);
    }

    public List<SupplierReturnItemResponse> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SupplierReturnItemResponse getItemById(Long id) {
        SupplierReturnItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Return item not found with id " + id));
        return mapToResponse(item);
    }

    public SupplierReturnItemResponse updateItem(Long id, SupplierReturnItemRequest request) {
        SupplierReturnItem existing = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Return item not found with id " + id));

        SupplierReturn supplierReturn = returnRepository.findById(request.getSupplierReturnId())
                .orElseThrow(() -> new RuntimeException("Supplier Return not found"));
        
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        existing.setSupplierReturn(supplierReturn);
        existing.setBatch(batch);
        existing.setQtyReturned(request.getQtyReturned());
        existing.setUnitCostRefunded(request.getUnitCostRefunded());
        existing.setReason(request.getReason());

        SupplierReturnItem updated = itemRepository.save(existing);
        return mapToResponse(updated);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Return item not found with id " + id);
        }
        itemRepository.deleteById(id);
    }

    private SupplierReturnItemResponse mapToResponse(SupplierReturnItem item) {
        return SupplierReturnItemResponse.builder()
                .id(item.getId())
                .supplierReturnId(item.getSupplierReturn().getId())
                .batchId(item.getBatch().getId())
                .batchNumber(item.getBatch().getBatchNumber())
                .qtyReturned(item.getQtyReturned())
                .unitCostRefunded(item.getUnitCostRefunded())
                .reason(item.getReason())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private SupplierReturnItem mapToEntity(SupplierReturnItemRequest request) {
        SupplierReturn supplierReturn = returnRepository.findById(request.getSupplierReturnId())
                .orElseThrow(() -> new RuntimeException("Supplier Return not found"));

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        return SupplierReturnItem.builder()
                .supplierReturn(supplierReturn)
                .batch(batch)
                .qtyReturned(request.getQtyReturned())
                .unitCostRefunded(request.getUnitCostRefunded())
                .reason(request.getReason())
                .build();
    }
}