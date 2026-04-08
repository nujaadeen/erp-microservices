package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.GRNRequest;
import com.zamzamsuper.inventory_service.dto.GRNResponse;
import com.zamzamsuper.inventory_service.model.GRN;
import com.zamzamsuper.inventory_service.model.Supplier;
import com.zamzamsuper.inventory_service.repository.GRNRepository;
import com.zamzamsuper.inventory_service.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GRNService {

    private final GRNRepository grnRepository;
    private final SupplierRepository supplierRepository;

    public GRNResponse createGRN(GRNRequest request) {
        GRN grn = mapToEntity(request);
        GRN savedGrn = grnRepository.save(grn);
        return mapToResponse(savedGrn);
    }

    public List<GRNResponse> getAllGRNs() {
        return grnRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GRNResponse getGRNById(Long id) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id " + id));
        return mapToResponse(grn);
    }

    public GRNResponse updateGRN(Long id, GRNRequest request) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id " + id));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        grn.setSupplier(supplier);
        grn.setInvoiceNum(request.getInvoiceNum());
        grn.setTotalAmount(request.getTotalAmount());

        GRN updated = grnRepository.save(grn);
        return mapToResponse(updated);
    }

    public void deleteGRN(Long id) {
        if (!grnRepository.existsById(id)) {
            throw new RuntimeException("GRN not found with id " + id);
        }
        grnRepository.deleteById(id);
    }

    private GRNResponse mapToResponse(GRN grn) {
        return GRNResponse.builder()
                .id(grn.getId())
                .supplierId(grn.getSupplier().getId())
                .supplierName(grn.getSupplier().getName())
                .invoiceNum(grn.getInvoiceNum())
                .totalAmount(grn.getTotalAmount())
                .createdAt(grn.getCreatedAt())
                .updatedAt(grn.getUpdatedAt())
                .build();
    }

    private GRN mapToEntity(GRNRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return GRN.builder()
                .supplier(supplier)
                .invoiceNum(request.getInvoiceNum())
                .totalAmount(request.getTotalAmount())
                .build();
    }
}