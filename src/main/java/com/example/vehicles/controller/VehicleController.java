package com.example.vehicles.controller;

import com.example.vehicles.model.Vehicle;
import com.example.vehicles.model.VehicleUpdateRequest;
import com.example.vehicles.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.addVehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @RequestBody VehicleUpdateRequest vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @GetMapping("/search")
    public Page<Vehicle> searchVehicles(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            Pageable pageable) {
        if (field != null && value != null) {
            return vehicleService.searchBySingleField(field, value, pageable);
        } else {
            return vehicleService.searchWithAndConditions(manufacturer, model, type, description, minPrice, maxPrice, minYear,maxYear, pageable);
        }
    }

        @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
