package com.example.customerservice.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface SavedVehicleRepository extends JpaRepository<SavedVehicle, UUID> {
    List<SavedVehicle> findByCustomerProfileId(UUID customerProfileId);
}
