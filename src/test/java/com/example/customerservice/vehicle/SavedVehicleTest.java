package com.example.customerservice.vehicle;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SavedVehicleTest {
    @Test
    void createsVehicleOwnedByOneCustomerProfile() {
        UUID customerProfileId = UUID.randomUUID();

        SavedVehicle vehicle = SavedVehicle.create(
                customerProfileId,
                "Toyota",
                "Corolla",
                2020,
                "1.8L",
                null
        );

        assertThat(vehicle.getCustomerProfileId()).isEqualTo(customerProfileId);
        assertThat(vehicle.isPrimaryVehicle()).isFalse();
    }

    @Test
    void canBeMarkedAsTheCustomersPrimaryVehicle() {
        SavedVehicle vehicle = SavedVehicle.create(UUID.randomUUID(), "Toyota", "Corolla", 2020, "1.8L", null);

        vehicle.makePrimary();

        assertThat(vehicle.isPrimaryVehicle()).isTrue();
    }
}
