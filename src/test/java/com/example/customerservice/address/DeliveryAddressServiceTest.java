package com.example.customerservice.address;

import com.example.customerservice.profile.CustomerProfile;
import com.example.customerservice.profile.CustomerProfileRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryAddressServiceTest {
    @Test
    void addsAddressForTheAuthenticatedCustomersProfile() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        CustomerProfileRepository profiles = mock(CustomerProfileRepository.class);
        DeliveryAddressRepository addresses = mock(DeliveryAddressRepository.class);
        when(profiles.findByKeycloakUserId(subject)).thenReturn(Optional.of(profile));
        when(addresses.save(any(DeliveryAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        DeliveryAddressService service = new DeliveryAddressService(profiles, addresses);

        DeliveryAddress address = service.add(subject, "12 Main Street", "Harare", "Zimbabwe", "00000");

        assertThat(address.getCustomerProfileId()).isEqualTo(profile.getId());
        verify(addresses).save(any(DeliveryAddress.class));
    }

    @Test
    void listsOnlyAddressesForTheAuthenticatedCustomersProfile() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        DeliveryAddress address = DeliveryAddress.create(profile.getId(), "12 Main Street", "Harare", "Zimbabwe", "00000");
        CustomerProfileRepository profiles = mock(CustomerProfileRepository.class);
        DeliveryAddressRepository addresses = mock(DeliveryAddressRepository.class);
        when(profiles.findByKeycloakUserId(subject)).thenReturn(Optional.of(profile));
        when(addresses.findByCustomerProfileId(profile.getId())).thenReturn(List.of(address));
        DeliveryAddressService service = new DeliveryAddressService(profiles, addresses);

        assertThat(service.list(subject)).containsExactly(address);
    }
}
