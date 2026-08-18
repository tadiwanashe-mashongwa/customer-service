package com.example.customerservice.address;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.UUID;

@Entity
@Table(name = "delivery_addresses")
public class DeliveryAddress {
    @Id private UUID id;
    private UUID customerProfileId;
    private String line1;
    private String city;
    private String country;
    private String postalCode;
    private boolean defaultAddress;
    @Version private long version;

    protected DeliveryAddress() { }

    private DeliveryAddress(UUID customerProfileId, String line1, String city, String country, String postalCode) {
        this.id = UUID.randomUUID();
        this.customerProfileId = customerProfileId;
        this.line1 = line1;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public static DeliveryAddress create(UUID customerProfileId, String line1, String city, String country, String postalCode) {
        return new DeliveryAddress(customerProfileId, line1, city, country, postalCode);
    }

    public UUID getCustomerProfileId() { return customerProfileId; }
    public boolean isDefaultAddress() { return defaultAddress; }
}
