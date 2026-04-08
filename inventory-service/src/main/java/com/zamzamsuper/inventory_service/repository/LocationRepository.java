package com.zamzamsuper.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zamzamsuper.inventory_service.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}