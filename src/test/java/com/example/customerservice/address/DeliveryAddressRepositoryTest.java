package com.example.customerservice.address;

import com.example.customerservice.profile.CustomerProfile;
import com.example.customerservice.profile.CustomerProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class DeliveryAddressRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DeliveryAddressRepository addresses;

    @Autowired
    private CustomerProfileRepository profiles;

    @Test
    void findsAddressesOnlyForTheRequestedCustomerProfile() {
        CustomerProfile profile = profiles.save(CustomerProfile.create(UUID.randomUUID(), "Tadi", "Mashongwa", "tadi@example.com", null));
        CustomerProfile anotherProfile = profiles.save(CustomerProfile.create(UUID.randomUUID(), "Tariro", "Moyo", "tariro@example.com", null));
        DeliveryAddress matching = addresses.save(DeliveryAddress.create(profile.getId(), "12 Main Street", "Harare", "Zimbabwe", "00000"));
        addresses.save(DeliveryAddress.create(anotherProfile.getId(), "34 Oak Avenue", "Bulawayo", "Zimbabwe", "00000"));

        assertThat(addresses.findByCustomerProfileId(profile.getId())).containsExactly(matching);
    }
}
