package com.example.customerservice.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateSavedVehicleRequest(
        @NotBlank String make,
        @NotBlank String model,
        @Min(1886) int modelYear,
        String engine,
        String vin
) {
}
