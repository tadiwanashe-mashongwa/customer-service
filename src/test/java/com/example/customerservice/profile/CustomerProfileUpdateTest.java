package com.example.customerservice.profile;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerProfileUpdateTest {
    @Test
    void updatesOnlyCustomerProfileDetails() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Old", "Name", "old@example.com", null);

        profile.update("Tadi", "Mashongwa", "tadi@example.com", "+263771000000");

        assertThat(profile.getKeycloakUserId()).isEqualTo(subject);
        assertThat(profile.getEmail()).isEqualTo("tadi@example.com");
        assertThat(profile.getPhoneNumber()).isEqualTo("+263771000000");
    }
}
