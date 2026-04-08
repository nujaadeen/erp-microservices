package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.SupplierReturnRequest;
import com.zamzamsuper.inventory_service.dto.SupplierReturnResponse;
import com.zamzamsuper.inventory_service.model.GRN;
import com.zamzamsuper.inventory_service.model.Supplier;
import com.zamzamsuper.inventory_service.model.SupplierReturn;
import com.zamzamsuper.inventory_service.repository.GRNRepository;
import com.zamzamsuper.inventory_service.repository.SupplierRepository;
import com.zamzamsuper.inventory_service.repository.SupplierReturnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierReturnService {

    private final SupplierReturnRepository returnRepository;
    private final SupplierRepository supplierRepository;
    private final GRNRepository grnRepository;

    public SupplierReturnResponse createReturn(SupplierReturnRequest request) {
        SupplierReturn supplierReturn = mapToEntity(request);
        SupplierReturn savedSupplierReturn = returnRepository.save(supplierReturn);
        return mapToResponse(savedSupplierReturn);
    }

    public List<SupplierReturnResponse> getAllReturns() {
        return returnRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SupplierReturnResponse getReturnById(Long id) {
        SupplierReturn supplierReturn = returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier Return not found with id " + id));
        return mapToResponse(supplierReturn);
    }

    public SupplierReturnResponse updateReturn(Long id, SupplierReturnRequest request) {
        SupplierReturn existing = returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier Return not found with id " + id));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // GRN is optional in your model, so we handle nulls safely
        GRN grn = (request.getOriginalGrnId() != null) ? 
                grnRepository.findById(request.getOriginalGrnId()).orElse(null) : null;

        existing.setSupplier(supplier);
        existing.setOriginalGrn(grn);
        existing.setReturnDate(request.getReturnDate());
        existing.setTotalRefundValue(request.getTotalRefundValue());
        existing.setReturnStatus(request.getReturnStatus());

        SupplierReturn updated = returnRepository.save(existing);
        return mapToResponse(updated);
    }

    public void deleteReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new RuntimeException("Supplier Return not found with id " + id);
        }
        returnRepository.deleteById(id);
    }

    private SupplierReturnResponse mapToResponse(SupplierReturn supplierReturn) {
        return SupplierReturnResponse.builder()
                .id(supplierReturn.getId())
                .supplierId(supplierReturn.getSupplier().getId())
                .supplierName(supplierReturn.getSupplier().getName())
                .originalGrnId(supplierReturn.getOriginalGrn() != null ? supplierReturn.getOriginalGrn().getId() : null)
                .returnDate(supplierReturn.getReturnDate())
                .totalRefundValue(supplierReturn.getTotalRefundValue())
                .returnStatus(supplierReturn.getReturnStatus())
                .createdAt(supplierReturn.getCreatedAt())
                .updatedAt(supplierReturn.getUpdatedAt())
                .build();
    }

    private SupplierReturn mapToEntity(SupplierReturnRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        GRN grn = (request.getOriginalGrnId() != null) ? 
                grnRepository.findById(request.getOriginalGrnId()).orElse(null) : null;

        return SupplierReturn.builder()
                .supplier(supplier)
                .originalGrn(grn)
                .returnDate(request.getReturnDate())
                .totalRefundValue(request.getTotalRefundValue())
                .returnStatus(request.getReturnStatus())
                .build();
    }
}