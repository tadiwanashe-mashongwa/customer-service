package com.example.customerservice.address;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DeliveryAddressControllerTest {
    @Test
    void addsAddressForTheAuthenticatedSubject() {
        UUID subject = UUID.randomUUID();
        DeliveryAddressService service = mock(DeliveryAddressService.class);
        DeliveryAddress address = DeliveryAddress.create(UUID.randomUUID(), "12 Main Street", "Harare", "Zimbabwe", "00000");
        when(service.add(eq(subject), anyString(), anyString(), anyString(), anyString())).thenReturn(address);
        DeliveryAddressController controller = new DeliveryAddressController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        DeliveryAddressResponse response = controller.add(new CreateDeliveryAddressRequest("12 Main Street", "Harare", "Zimbabwe", "00000"), jwt);

        assertThat(response.line1()).isEqualTo("12 Main Street");
        verify(service).add(eq(subject), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void makesOnlyTheAuthenticatedCustomersAddressDefault() {
        UUID subject = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        DeliveryAddressService service = mock(DeliveryAddressService.class);
        DeliveryAddressController controller = new DeliveryAddressController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        controller.makeDefault(addressId, jwt);

        verify(service).makeDefault(subject, addressId);
    }

    @Test
    void removesOnlyTheAuthenticatedCustomersAddress() {
        UUID subject = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        DeliveryAddressService service = mock(DeliveryAddressService.class);
        DeliveryAddressController controller = new DeliveryAddressController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        controller.remove(addressId, jwt);

        verify(service).remove(subject, addressId);
    }
}
