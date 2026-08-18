package com.example.customerservice.vehicle;

import com.example.customerservice.profile.CustomerProfile;
import com.example.customerservice.profile.CustomerProfileRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SavedVehicleServiceTest {
    @Test
    void addsVehicleForTheAuthenticatedCustomersProfile() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        CustomerProfileRepository profiles = mock(CustomerProfileRepository.class);
        SavedVehicleRepository vehicles = mock(SavedVehicleRepository.class);
        when(profiles.findByKeycloakUserId(subject)).thenReturn(Optional.of(profile));
        when(vehicles.save(any(SavedVehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SavedVehicleService service = new SavedVehicleService(profiles, vehicles);

        SavedVehicle vehicle = service.add(subject, "Toyota", "Corolla", 2020, "1.8L", null);

        assertThat(vehicle.getCustomerProfileId()).isEqualTo(profile.getId());
        verify(vehicles).save(any(SavedVehicle.class));
    }

    @Test
    void listsOnlyVehiclesForTheAuthenticatedCustomersProfile() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        SavedVehicle vehicle = SavedVehicle.create(profile.getId(), "Toyota", "Corolla", 2020, "1.8L", null);
        CustomerProfileRepository profiles = mock(CustomerProfileRepository.class);
        SavedVehicleRepository vehicles = mock(SavedVehicleRepository.class);
        when(profiles.findByKeycloakUserId(subject)).thenReturn(Optional.of(profile));
        when(vehicles.findByCustomerProfileId(profile.getId())).thenReturn(List.of(vehicle));
        SavedVehicleService service = new SavedVehicleService(profiles, vehicles);

        assertThat(service.list(subject)).containsExactly(vehicle);
    }

    @Test
    void makesOnlyTheSelectedCustomersVehiclePrimary() {
        UUID subject = UUID.randomUUID();
        CustomerProfile profile = CustomerProfile.create(subject, "Tadi", "Mashongwa", "tadi@example.com", null);
        SavedVehicle previous = SavedVehicle.create(profile.getId(), "Honda", "Civic", 2019, "1.5L", null);
        previous.makePrimary();
        SavedVehicle selected = SavedVehicle.create(profile.getId(), "Toyota", "Corolla", 2020, "1.8L", null);
        CustomerProfileRepository profiles = mock(CustomerProfileRepository.class);
        SavedVehicleRepository vehicles = mock(SavedVehicleRepository.class);
        when(profiles.findByKeycloakUserId(subject)).thenReturn(Optional.of(profile));
        when(vehicles.findByCustomerProfileId(profile.getId())).thenReturn(List.of(previous, selected));
        SavedVehicleService service = new SavedVehicleService(profiles, vehicles);

        service.makePrimary(subject, selected.getId());

        assertThat(previous.isPrimaryVehicle()).isFalse();
        assertThat(selected.isPrimaryVehicle()).isTrue();
        verify(vehicles).saveAll(any());
    }
}
