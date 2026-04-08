package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.SupplierRequest;
import com.zamzamsuper.inventory_service.dto.SupplierResponse;
import com.zamzamsuper.inventory_service.model.Supplier;
import com.zamzamsuper.inventory_service.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = mapToEntity(request);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return mapToResponse(savedSupplier);
    }

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
        return mapToResponse(supplier);
    }

    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));

        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());

        Supplier updated = supplierRepository.save(supplier);
        return mapToResponse(updated);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    private Supplier mapToEntity(SupplierRequest request) {
        return Supplier.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .build();
    }
}