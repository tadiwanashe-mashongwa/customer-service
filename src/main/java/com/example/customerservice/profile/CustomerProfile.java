package com.example.customerservice.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    private UUID id;
    private UUID keycloakUserId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;
    @Version
    private long version;

    protected CustomerProfile() { }

    private CustomerProfile(UUID keycloakUserId, String firstName, String lastName, String email, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.keycloakUserId = keycloakUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static CustomerProfile create(UUID keycloakUserId, String firstName, String lastName, String email, String phoneNumber) {
        return new CustomerProfile(keycloakUserId, firstName, lastName, email, phoneNumber);
    }

    public UUID getKeycloakUserId() { return keycloakUserId; }
    public String getEmail() { return email; }
    public long getVersion() { return version; }
}
