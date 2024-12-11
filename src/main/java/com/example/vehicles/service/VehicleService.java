package com.example.vehicles.service;

import com.example.vehicles.model.Vehicle;
import com.example.vehicles.model.VehicleUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(Vehicle vehicle);

    List<Vehicle> getAllVehicles();

    Vehicle getVehicleById(Long id);

    Vehicle updateVehicle(Long id, VehicleUpdateRequest vehicle);

    void deleteVehicle(Long id);

    Page<Vehicle> searchBySingleField(String field, String value, Pageable pageable);

    Page<Vehicle> searchWithAndConditions(
            String manufacturer,
            String model,
            String type,
            String description,
            Double minPrice,
            Double maxPrice,
            Integer minYear,
            Integer maxYear,
            Pageable pageable);
}
