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
}
