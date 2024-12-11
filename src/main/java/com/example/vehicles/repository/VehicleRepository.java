package com.example.vehicles.repository;

import com.example.vehicles.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Page<Vehicle> findByManufacturerIgnoreCase(String manufacturer, Pageable pageable);
    Page<Vehicle> findByModelIgnoreCase(String model, Pageable pageable);
    Page<Vehicle> findByTypeIgnoreCase(String type, Pageable pageable);
    Page<Vehicle> findByDescriptionIgnoreCase(String description, Pageable pageable);
    Page<Vehicle> findByPrice(Double price, Pageable pageable); // Exact price match

    @Query("SELECT v FROM Vehicle v WHERE " +
            "(:manufacturer IS NULL OR LOWER(v.manufacturer) LIKE LOWER(CONCAT('%', :manufacturer, '%'))) AND " +
            "(:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
            "(:type IS NULL OR LOWER(v.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
            "(:description IS NULL OR LOWER(v.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:minPrice IS NULL OR v.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR v.price <= :maxPrice)")
    Page<Vehicle> searchWithAndConditions(
            String manufacturer,
            String model,
            String type,
            String description,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );
}
