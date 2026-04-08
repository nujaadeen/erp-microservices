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

import com.zamzamsuper.inventory_service.dto.LocationRequest;
import com.zamzamsuper.inventory_service.dto.LocationResponse;
import com.zamzamsuper.inventory_service.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/location")
public class LocationController {

    private final LocationService locationService;

    // Create a location
    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@RequestBody LocationRequest request) {
        LocationResponse created = locationService.createLocation(request);
        return ResponseEntity.ok(created);
    }

    // Get all locations
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        List<LocationResponse> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    // Get location by ID
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        LocationResponse location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    // Update a location
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable Long id,
            @RequestBody LocationRequest request) {
        LocationResponse updated = locationService.updateLocation(id, request);
        return ResponseEntity.ok(updated);
    }

    // Delete a location
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}