package com.zamzamsuper.inventory_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.inventory_service.dto.LocationRequest;
import com.zamzamsuper.inventory_service.dto.LocationResponse;
import com.zamzamsuper.inventory_service.model.Location;
import com.zamzamsuper.inventory_service.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    // Create
    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = mapToEntity(locationRequest);
        Location savedLocation = locationRepository.save(location);
        return mapToResponse(savedLocation);
    }

    // Read all
    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Read by ID
    public LocationResponse getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id " + id));
        return mapToResponse(location);
    }

    // Update
    public LocationResponse updateLocation(Long id, LocationRequest locationRequest) {
        Location updatedLocation = mapToEntity(locationRequest);

        Location saved = locationRepository.findById(id).map(location -> {
            location.setName(updatedLocation.getName());
            location.setActive(updatedLocation.isActive());
            return locationRepository.save(location);
        }).orElseThrow(() -> new RuntimeException("Location not found with id " + id));

        return mapToResponse(saved);
    }

    // Delete
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location not found with id " + id);
        }
        locationRepository.deleteById(id);
    }

    // ---------------------------
    // Private helper methods
    // ---------------------------

    // Convert entity -> response DTO
    private LocationResponse mapToResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .active(location.isActive())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }

    // Convert request DTO -> entity
    private Location mapToEntity(LocationRequest locationRequest) {
        return Location.builder()
                .name(locationRequest.getName())
                .active(locationRequest.getActive())
                .build();
    }
}