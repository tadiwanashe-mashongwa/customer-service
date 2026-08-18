package com.example.customerservice.vehicle;

import java.util.UUID;

public record SavedVehicleResponse(
        UUID id,
        String make,
        String model,
        int modelYear,
        String engine,
        String vin,
        boolean primaryVehicle
) {
    static SavedVehicleResponse from(SavedVehicle vehicle) {
        return new SavedVehicleResponse(
                vehicle.getId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getModelYear(),
                vehicle.getEngine(),
                vehicle.getVin(),
                vehicle.isPrimaryVehicle()
        );
    }
}
