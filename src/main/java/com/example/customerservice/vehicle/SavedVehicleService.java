package com.example.customerservice.vehicle;

import com.example.customerservice.profile.CustomerProfile;
import com.example.customerservice.profile.CustomerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class SavedVehicleService {
    private final CustomerProfileRepository profiles;
    private final SavedVehicleRepository vehicles;

    public SavedVehicleService(CustomerProfileRepository profiles, SavedVehicleRepository vehicles) {
        this.profiles = profiles;
        this.vehicles = vehicles;
    }

    public SavedVehicle add(UUID keycloakUserId, String make, String model, int modelYear, String engine, String vin) {
        CustomerProfile profile = profiles.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Customer profile not found"));
        return vehicles.save(SavedVehicle.create(profile.getId(), make, model, modelYear, engine, vin));
    }

    public List<SavedVehicle> list(UUID keycloakUserId) {
        CustomerProfile profile = profiles.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Customer profile not found"));
        return vehicles.findByCustomerProfileId(profile.getId());
    }

    public void makePrimary(UUID keycloakUserId, UUID vehicleId) {
        List<SavedVehicle> customerVehicles = list(keycloakUserId);
        SavedVehicle selected = customerVehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(vehicleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Saved vehicle not found"));
        customerVehicles.forEach(SavedVehicle::clearPrimary);
        selected.makePrimary();
        vehicles.saveAll(customerVehicles);
    }

    public void remove(UUID keycloakUserId, UUID vehicleId) {
        SavedVehicle vehicle = list(keycloakUserId).stream()
                .filter(savedVehicle -> savedVehicle.getId().equals(vehicleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Saved vehicle not found"));
        vehicles.delete(vehicle);
    }
}
