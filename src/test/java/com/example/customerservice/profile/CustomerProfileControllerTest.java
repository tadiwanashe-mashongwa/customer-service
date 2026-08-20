package com.example.customerservice.profile;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomerProfileControllerTest {

    @Test
    void returnsProfileForTheAuthenticatedJwtSubject() {
        UUID subject = UUID.randomUUID();
        CustomerProfileService service = mock(CustomerProfileService.class);
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        when(service.getOrCreate(eq(subject), anyString(), anyString(), anyString(), isNull())).thenReturn(profile);
        CustomerProfileController controller = new CustomerProfileController(service);
        Jwt jwt = new Jwt(subject.toString(), Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString(), "given_name", "Tadi", "family_name", "Mashongwa", "email", "tadi@example.com"));

        CustomerProfileResponse response = controller.me(jwt);

        assertThat(response.keycloakUserId()).isEqualTo(subject);
        assertThat(response.email()).isEqualTo("tadi@example.com");
    }

    @Test
    void updatesProfileForTheAuthenticatedJwtSubject() {
        UUID subject = UUID.randomUUID();
        CustomerProfileService service = mock(CustomerProfileService.class);
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", "+263771000000");
        when(service.update(eq(subject), any())).thenReturn(profile);
        CustomerProfileController controller = new CustomerProfileController(service);
        Jwt jwt = new Jwt(subject.toString(), Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        CustomerProfileResponse response = controller.update(new UpdateCustomerProfileRequest("Tadi", "Mashongwa", "tadi@example.com", "+263771000000"), jwt);

        assertThat(response.keycloakUserId()).isEqualTo(subject);
        verify(service).update(eq(subject), any());
    }

    @Test
    void returnsThePaymentContactForTheRequestedCustomerToPaymentService() {
        UUID customerId = UUID.randomUUID();
        CustomerProfileService service = mock(CustomerProfileService.class);
        when(service.paymentPhoneNumber(customerId)).thenReturn("+263771000000");
        CustomerProfileController controller = new CustomerProfileController(service);
        Jwt jwt = new Jwt("payment-service", Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "none"), Map.of("sub", "payment-service", "azp", "payment-service"));

        PaymentContactResponse response = controller.paymentContact(customerId, jwt);

        assertThat(response.phoneNumber()).isEqualTo("+263771000000");
        verify(service).paymentPhoneNumber(customerId);
    }

    @Test
    void rejectsPaymentContactRequestsFromOtherClients() {
        CustomerProfileService service = mock(CustomerProfileService.class);
        CustomerProfileController controller = new CustomerProfileController(service);
        Jwt jwt = new Jwt("another-service", Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "none"), Map.of("sub", "another-service", "azp", "another-service"));

        assertThatThrownBy(() -> controller.paymentContact(UUID.randomUUID(), jwt))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class);
        verifyNoInteractions(service);
    }
}
