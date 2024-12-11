package com.example.vehicles.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1886, message = "Year must be greater than 1886")  // First car invented in 1886
    @Max(value = 2100, message = "Year must be realistic")
    @Max(value = 2023, message = "Year must be realistic")
    @Column(name = "\"year\"", nullable = false)
    private Integer year;

    @NotBlank(message = "Type is required")
    private String type;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private Double price;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

}
