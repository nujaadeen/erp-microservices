package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.StockRequest;
import com.zamzamsuper.inventory_service.dto.StockResponse;
import com.zamzamsuper.inventory_service.model.Location;
import com.zamzamsuper.inventory_service.model.Stock;
import com.zamzamsuper.inventory_service.repository.LocationRepository;
import com.zamzamsuper.inventory_service.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;

    public StockResponse createStock(StockRequest request) {
        Stock stock = mapToEntity(request);
        Stock savedStock = stockRepository.save(stock);
        return mapToResponse(savedStock);
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StockResponse getStockById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock record not found with id " + id));
        return mapToResponse(stock);
    }

    public StockResponse updateStock(Long id, StockRequest request) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock record not found with id " + id));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        existingStock.setProductSku(request.getProductSku());
        existingStock.setLocation(location);
        existingStock.setQuantityOnHand(request.getQuantityOnHand());
        existingStock.setReorderLevel(request.getReorderLevel());

        Stock updated = stockRepository.save(existingStock);
        return mapToResponse(updated);
    }

    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Stock record not found with id " + id);
        }
        stockRepository.deleteById(id);
    }

    private StockResponse mapToResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .productSku(stock.getProductSku())
                .locationId(stock.getLocation().getId())
                .locationName(stock.getLocation().getName())
                .quantityOnHand(stock.getQuantityOnHand())
                .reorderLevel(stock.getReorderLevel())
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .build();
    }

    private Stock mapToEntity(StockRequest request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        return Stock.builder()
                .productSku(request.getProductSku())
                .location(location)
                .quantityOnHand(request.getQuantityOnHand())
                .reorderLevel(request.getReorderLevel())
                .build();
    }
}