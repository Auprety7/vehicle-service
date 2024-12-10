package com.example.service;

import com.example.model.Vehicle;
import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(Long id);
    Vehicle updateVehicle(Long id, Vehicle vehicle);
    void deleteVehicle(Long id);
}
