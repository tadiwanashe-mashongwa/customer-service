package com.example.customerservice.profile;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerProfileService {
    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) { this.repository = repository; }

    public CustomerProfile getOrCreate(UUID keycloakUserId, String firstName, String lastName, String email, String phoneNumber) {
        return repository.findByKeycloakUserId(keycloakUserId)
                .orElseGet(() -> repository.save(CustomerProfile.create(keycloakUserId, firstName, lastName, email, phoneNumber)));
    }

    public CustomerProfile update(UUID keycloakUserId, UpdateCustomerProfileRequest request) {
        CustomerProfile profile = repository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Customer profile not found"));
        profile.update(request.firstName(), request.lastName(), request.email(), request.phoneNumber());
        return repository.save(profile);
    }
}
