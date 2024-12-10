package com.example.vehicles.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class VehicleUpdateRequest {

    private Optional<@NotBlank String> manufacturer = Optional.empty();
    private Optional<@NotBlank String> model = Optional.empty();
    private Optional<@Min(1886) @Max(2100) Integer> year = Optional.empty();
    private Optional<@NotBlank String> type = Optional.empty();
    private Optional<@DecimalMin("0.0") Double> price = Optional.empty();
    private Optional<@Size(max = 500) String> description = Optional.empty();
}
