package com.example.customerservice.profile;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerProfileServiceTest {

    @Test
    void returnsExistingProfileForTheAuthenticatedKeycloakSubject() {
        UUID subject = UUID.randomUUID();
        CustomerProfile existing = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        CustomerProfileRepository repository = mock(CustomerProfileRepository.class);
        when(repository.findByKeycloakUserId(subject)).thenReturn(Optional.of(existing));
        CustomerProfileService service = new CustomerProfileService(repository);

        CustomerProfile profile = service.getOrCreate(subject, "Ignored", "User", "ignored@example.com", null);

        assertThat(profile).isSameAs(existing);
        verify(repository, never()).save(any());
    }

    @Test
    void createsAndPersistsProfileForAFirstTimeKeycloakSubject() {
        UUID subject = UUID.randomUUID();
        CustomerProfileRepository repository = mock(CustomerProfileRepository.class);
        when(repository.findByKeycloakUserId(subject)).thenReturn(Optional.empty());
        when(repository.save(any(CustomerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CustomerProfileService service = new CustomerProfileService(repository);

        CustomerProfile profile = service.getOrCreate(subject, "Tadi", "Mashongwa", "tadi@example.com", "+263771000000");

        assertThat(profile.getKeycloakUserId()).isEqualTo(subject);
        verify(repository).save(any(CustomerProfile.class));
    }
}
