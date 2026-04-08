package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.ProductPriceRequest;
import com.zamzamsuper.inventory_service.dto.ProductPriceResponse;
import com.zamzamsuper.inventory_service.model.Batch;
import com.zamzamsuper.inventory_service.model.ProductPrice;
import com.zamzamsuper.inventory_service.repository.BatchRepository;
import com.zamzamsuper.inventory_service.repository.ProductPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;
    private final BatchRepository batchRepository;

    public ProductPriceResponse createPrice(ProductPriceRequest request) {
        ProductPrice productPrice = mapToEntity(request);
        ProductPrice savedProductPrice = productPriceRepository.save(productPrice);
        return mapToResponse(savedProductPrice);
    }

    public List<ProductPriceResponse> getAllPrices() {
        return productPriceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductPriceResponse getPriceById(Long id) {
        ProductPrice price = productPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id " + id));
        return mapToResponse(price);
    }

    public ProductPriceResponse updatePrice(Long id, ProductPriceRequest request) {
        ProductPrice existingPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id " + id));

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        existingPrice.setBatch(batch);
        existingPrice.setPriceType(request.getPriceType());
        existingPrice.setActive(request.getActive());
        existingPrice.setMinQuantity(request.getMinQuantity());
        existingPrice.setPrice(request.getPrice());
        existingPrice.setMinPrice(request.getMinPrice());

        ProductPrice updated = productPriceRepository.save(existingPrice);
        return mapToResponse(updated);
    }

    public void deletePrice(Long id) {
        if (!productPriceRepository.existsById(id)) {
            throw new RuntimeException("Price not found with id " + id);
        }
        productPriceRepository.deleteById(id);
    }

    private ProductPriceResponse mapToResponse(ProductPrice price) {
        return ProductPriceResponse.builder()
                .id(price.getId())
                .batchId(price.getBatch().getId())
                .batchNumber(price.getBatch().getBatchNumber())
                .priceType(price.getPriceType())
                .active(price.getActive())
                .minQuantity(price.getMinQuantity())
                .price(price.getPrice())
                .minPrice(price.getMinPrice())
                .createdAt(price.getCreatedAt())
                .updatedAt(price.getUpdatedAt())
                .build();
    }

    private ProductPrice mapToEntity(ProductPriceRequest request) {
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        return ProductPrice.builder()
                .batch(batch)
                .priceType(request.getPriceType())
                .active(request.getActive())
                .minQuantity(request.getMinQuantity())
                .price(request.getPrice())
                .minPrice(request.getMinPrice())
                .build();
    }
}