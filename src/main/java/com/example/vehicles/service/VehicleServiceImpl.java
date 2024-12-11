package com.example.vehicles.service;

import com.example.vehicles.exception.*;
import com.example.vehicles.model.Vehicle;
import com.example.vehicles.model.VehicleUpdateRequest;
import com.example.vehicles.repository.VehicleRepository;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    private Validator validator;


    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        checkForInputExceptions(vehicle);
        System.out.println("here");
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Vehicle not found with id: " + id));
    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleUpdateRequest updateRequest) {

        Vehicle existingVehicle = getVehicleById(id);
        updateRequest.getManufacturer().ifPresent(existingVehicle::setManufacturer);
        updateRequest.getModel().ifPresent(existingVehicle::setModel);
        updateRequest.getYear().ifPresent(existingVehicle::setYear);
        updateRequest.getType().ifPresent(existingVehicle::setType);
        updateRequest.getPrice().ifPresent(existingVehicle::setPrice);
        updateRequest.getDescription().ifPresent(existingVehicle::setDescription);

        return vehicleRepository.save(existingVehicle);
    }

    private void checkForInputExceptions(Vehicle updatedVehicle) {
        Set<ConstraintViolation<Vehicle>> violations = validator.validate(updatedVehicle);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Vehicle> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new InputValidationException(errors);
        }
    }

    @Override
    public Page<Vehicle> searchBySingleField(String field, String value, Pageable pageable) {
        switch (field.toLowerCase()) {
            case "manufacturer":
                return vehicleRepository.findByManufacturerIgnoreCase(value, pageable);
            case "model":
                return vehicleRepository.findByModelIgnoreCase(value, pageable);
            case "type":
                return vehicleRepository.findByTypeIgnoreCase(value, pageable);
            case "description":
                return vehicleRepository.findByDescriptionIgnoreCase(value, pageable);
            case "price":
                try {
                    Double price = Double.parseDouble(value);
                    return vehicleRepository.findByPrice(price, pageable);
                } catch (NumberFormatException e) {
                    throw new InputValidationException("Price must be a valid number: " + value);
                }
            default:
                throw new InputValidationException("Invalid field for search: " + field);
        }
    }

    @Override
    public Page<Vehicle> searchWithAndConditions(
            String manufacturer,
            String model,
            String type,
            String description,
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {
        return vehicleRepository.searchWithAndConditions(manufacturer, model, type, description, minPrice, maxPrice, pageable);
    }

    @Override
    public void deleteVehicle(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new BadRequestException("Vehicle with ID " + id + " not found");
        }
    }
}
