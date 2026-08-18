package com.example.customerservice.vehicle;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SavedVehicleControllerTest {
    @Test
    void addsVehicleForTheAuthenticatedSubject() {
        UUID subject = UUID.randomUUID();
        SavedVehicleService service = mock(SavedVehicleService.class);
        SavedVehicle vehicle = SavedVehicle.create(UUID.randomUUID(), "Toyota", "Corolla", 2020, "1.8L", null);
        when(service.add(eq(subject), anyString(), anyString(), anyInt(), anyString(), eq(null))).thenReturn(vehicle);
        SavedVehicleController controller = new SavedVehicleController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        SavedVehicleResponse response = controller.add(new CreateSavedVehicleRequest("Toyota", "Corolla", 2020, "1.8L", null), jwt);

        assertThat(response.make()).isEqualTo("Toyota");
        verify(service).add(eq(subject), anyString(), anyString(), anyInt(), anyString(), eq(null));
    }

    @Test
    void listsVehiclesForTheAuthenticatedSubject() {
        UUID subject = UUID.randomUUID();
        SavedVehicleService service = mock(SavedVehicleService.class);
        SavedVehicle vehicle = SavedVehicle.create(UUID.randomUUID(), "Toyota", "Corolla", 2020, "1.8L", null);
        when(service.list(subject)).thenReturn(List.of(vehicle));
        SavedVehicleController controller = new SavedVehicleController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        assertThat(controller.list(jwt)).extracting(SavedVehicleResponse::make).containsExactly("Toyota");
    }

    @Test
    void makesOnlyTheAuthenticatedCustomersVehiclePrimary() {
        UUID subject = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        SavedVehicleService service = mock(SavedVehicleService.class);
        SavedVehicleController controller = new SavedVehicleController(service);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", subject.toString()));

        controller.makePrimary(vehicleId, jwt);

        verify(service).makePrimary(subject, vehicleId);
    }
}
