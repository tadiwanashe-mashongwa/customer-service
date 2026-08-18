package com.example.customerservice.profile;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
}
