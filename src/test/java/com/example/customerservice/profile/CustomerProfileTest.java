package com.example.customerservice.profile;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerProfileTest {

    @Test
    void createsProfileLinkedToTheKeycloakSubject() {
        UUID keycloakUserId = UUID.randomUUID();

        CustomerProfile profile = CustomerProfile.create(
                keycloakUserId, "Tadiwanashe", "Mashongwa", "tadi@example.com", "+263771000000"
        );

        assertThat(profile.getKeycloakUserId()).isEqualTo(keycloakUserId);
        assertThat(profile.getEmail()).isEqualTo("tadi@example.com");
        assertThat(profile.getVersion()).isZero();
    }
}
