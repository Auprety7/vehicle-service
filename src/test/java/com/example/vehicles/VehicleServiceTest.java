package com.example.vehicles;


import com.example.vehicles.exception.InputValidationException;
import com.example.vehicles.model.Vehicle;
import com.example.vehicles.model.VehicleUpdateRequest;
import com.example.vehicles.service.VehicleServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VehicleServiceApplication.class)
@ExtendWith(SpringExtension.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class VehicleServiceTest {

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Autowired
    private Validator validator;

    private Vehicle vehicle;

    @Test
    void addVehicleTest() {
        Vehicle vehicle = new Vehicle();
        vehicle.setManufacturer("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2020);
        vehicle.setType("Sedan");
        vehicle.setPrice(30000.00);
        vehicle.setDescription("Reliable midsize car");

        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        assertNotNull(savedVehicle);
        assertEquals("Toyota", savedVehicle.getManufacturer());
    }

    @Test
    void addVehicleExceptionForInvalidData() {
        Vehicle vehicle = new Vehicle();

        assertThrows(InputValidationException.class, () -> vehicleService.addVehicle(vehicle));
    }

    @Test
    void getVehicleByIdTest() {
        Vehicle vehicle = new Vehicle();
        vehicle.setManufacturer("Honda");
        vehicle.setModel("Civic");
        vehicle.setYear(2019);
        vehicle.setType("Sedan");
        vehicle.setPrice(25000.00);
        vehicle.setDescription("Compact car");

        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        Vehicle foundVehicle = vehicleService.getVehicleById(savedVehicle.getId());

        assertNotNull(foundVehicle);
        assertEquals("Honda", foundVehicle.getManufacturer());
    }

    @Test
    void getVehicleById_ShouldThrowExceptionIfNotFound() {
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleById(999L));
    }

    @Test
    void updateVehicleTest() {
        Vehicle vehicle = new Vehicle();
        vehicle.setManufacturer("Ford");
        vehicle.setModel("Focus");
        vehicle.setYear(2018);
        vehicle.setType("Hatchback");
        vehicle.setPrice(20000.00);
        vehicle.setDescription("Economical car");

        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);

        VehicleUpdateRequest updateRequest = new VehicleUpdateRequest();
        updateRequest.setManufacturer(Optional.of("Ford Updated"));
        updateRequest.setPrice(Optional.of(21000.00));

        Vehicle updatedVehicle = vehicleService.updateVehicle(savedVehicle.getId(), updateRequest);

        assertNotNull(updatedVehicle);
        assertEquals("Ford Updated", updatedVehicle.getManufacturer());
        assertEquals(21000.00, updatedVehicle.getPrice());
    }

    @Test
    void updateVehicleExceptionIfNotFound() {
        VehicleUpdateRequest updateRequest = new VehicleUpdateRequest();
        updateRequest.setManufacturer(Optional.of("Non-existent"));

        assertThrows(RuntimeException.class, () -> vehicleService.updateVehicle(999L, updateRequest));
    }

    @Test
    void deleteVehicleTest() {
        Vehicle vehicle = new Vehicle();
        vehicle.setManufacturer("Chevrolet");
        vehicle.setModel("Malibu");
        vehicle.setYear(2017);
        vehicle.setType("Sedan");
        vehicle.setPrice(15000.00);
        vehicle.setDescription("Reliable car");

        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);

        vehicleService.deleteVehicle(savedVehicle.getId());

        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleById(savedVehicle.getId()));
    }

    @Test
    void deleteVehicleThrowExceptionIfNotFound() {
        assertThrows(RuntimeException.class, () -> vehicleService.deleteVehicle(999L));
    }

    @Test
    void searchBySingleFieldTest() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setManufacturer("BMW");
        vehicle1.setModel("X5");
        vehicle1.setYear(2022);
        vehicle1.setType("SUV");
        vehicle1.setPrice(60000.00);
        vehicle1.setDescription("Luxury SUV");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setManufacturer("BMW");
        vehicle2.setModel("3 Series");
        vehicle2.setYear(2021);
        vehicle2.setType("Sedan");
        vehicle2.setPrice(50000.00);
        vehicle2.setDescription("Luxury Sedan");

        vehicleService.addVehicle(vehicle1);
        vehicleService.addVehicle(vehicle2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> result = vehicleService.searchBySingleField("type", "sedan", pageable);
        Page<Vehicle> resultManufacturer = vehicleService.searchBySingleField("manufacturer", "BMW", pageable);

        assertEquals(1, result.get().count());
        assertEquals(2, resultManufacturer.get().count());
    }
}
