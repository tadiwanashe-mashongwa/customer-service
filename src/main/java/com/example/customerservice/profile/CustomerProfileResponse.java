package com.example.customerservice.profile;

import java.util.UUID;

public record CustomerProfileResponse(UUID keycloakUserId, String email) {
    static CustomerProfileResponse from(CustomerProfile profile) {
        return new CustomerProfileResponse(profile.getKeycloakUserId(), profile.getEmail());
    }
}
