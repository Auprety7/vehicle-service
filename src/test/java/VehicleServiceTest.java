import com.example.vehicles.model.Vehicle;
import com.example.vehicles.repository.VehicleRepository;
import com.example.vehicles.model.VehicleUpdateRequest;
import com.example.vehicles.service.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

        @Mock
        private VehicleRepository vehicleRepository;

        @Mock
        private Validator validator;

        @InjectMocks
        private VehicleServiceImpl vehicleService;

        private Vehicle vehicle;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            vehicle = new Vehicle();
            vehicle.setId(1L);
            vehicle.setManufacturer("Toyota");
            vehicle.setModel("Camry");
            vehicle.setYear(2022);
            vehicle.setType("Sedan");
            vehicle.setPrice(30000.0);
            vehicle.setDescription("Reliable family car");
        }


        @Test
        void testCreateVehicle() {
            when(validator.validate(vehicle)).thenReturn(Collections.emptySet());
            when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

            Vehicle savedVehicle = vehicleService.addVehicle(vehicle);

            assertNotNull(savedVehicle);
            assertEquals("Toyota", savedVehicle.getManufacturer());
            verify(validator, times(1)).validate(vehicle);
            verify(vehicleRepository, times(1)).save(vehicle);
        }

        // Read All
        @Test
        void testGetAllVehicles() {
            when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle));

            List<Vehicle> vehicles = vehicleService.getAllVehicles();

            assertNotNull(vehicles);
            assertEquals(1, vehicles.size());
            assertEquals("Toyota", vehicles.get(0).getManufacturer());
            verify(vehicleRepository, times(1)).findAll();
        }

        // Read By ID
        @Test
        void testGetVehicleById() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

            Vehicle foundVehicle = vehicleService.getVehicleById(1L);

            assertNotNull(foundVehicle);
            assertEquals(1L, foundVehicle.getId());
            assertEquals("Toyota", foundVehicle.getManufacturer());
            verify(vehicleRepository, times(1)).findById(1L);
        }

        @Test
        void testGetVehicleByIdNotFound() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                vehicleService.getVehicleById(1L);
            });

            assertEquals("Vehicle not found with id: 1", exception.getMessage());
            verify(vehicleRepository, times(1)).findById(1L);
        }

        // Update
        @Test
        void testUpdateVehicle() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

            VehicleUpdateRequest updateRequest = new VehicleUpdateRequest();
            updateRequest.setPrice(Optional.of(35000.0));
            updateRequest.setDescription(Optional.of("Updated description"));

            Vehicle updatedVehicle = vehicleService.updateVehicle(1L, updateRequest);

            assertNotNull(updatedVehicle);
            assertEquals(35000.0, updatedVehicle.getPrice());
            assertEquals("Updated description", updatedVehicle.getDescription());
            verify(vehicleRepository, times(1)).findById(1L);
            verify(vehicleRepository, times(1)).save(vehicle);
        }

        @Test
        void testUpdateVehicleNotFound() {
            when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

            VehicleUpdateRequest updateRequest = new VehicleUpdateRequest();
            updateRequest.setPrice(Optional.of(35000.0));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                vehicleService.updateVehicle(2L, updateRequest);
            });

            assertEquals("Vehicle not found with id: 2", exception.getMessage());
            verify(vehicleRepository, times(1)).findById(2L);
            verify(vehicleRepository, never()).save(any());
        }

        // Delete
        @Test
        void testDeleteVehicle() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            doNothing().when(vehicleRepository).deleteById(1L);

            vehicleService.deleteVehicle(1L);

            verify(vehicleRepository, times(1)).findById(1L);
            verify(vehicleRepository, times(1)).deleteById(1L);
        }

        @Test
        void testDeleteVehicleNotFound() {
            when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

           assertThrows(RuntimeException.class, () -> {
                vehicleService.deleteVehicle(2L);
            });

            verify(vehicleRepository, times(1)).findById(2L);
            verify(vehicleRepository, never()).deleteById(any());
        }

}
