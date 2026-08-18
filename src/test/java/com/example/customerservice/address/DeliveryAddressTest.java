package com.example.customerservice.address;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryAddressTest {
    @Test
    void createsAddressOwnedByOneCustomerProfile() {
        UUID customerProfileId = UUID.randomUUID();

        DeliveryAddress address = DeliveryAddress.create(customerProfileId, "12 Main Street", "Harare", "Zimbabwe", "00000");

        assertThat(address.getCustomerProfileId()).isEqualTo(customerProfileId);
        assertThat(address.isDefaultAddress()).isFalse();
    }

    @Test
    void canBeMarkedAsTheCustomersDefaultAddress() {
        DeliveryAddress address = DeliveryAddress.create(UUID.randomUUID(), "12 Main Street", "Harare", "Zimbabwe", "00000");

        address.makeDefault();

        assertThat(address.isDefaultAddress()).isTrue();
    }
}
