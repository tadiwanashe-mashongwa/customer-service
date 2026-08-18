package com.example.customerservice.vehicle;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/customers/me/vehicles")
public class SavedVehicleController {
    private final SavedVehicleService service;

    public SavedVehicleController(SavedVehicleService service) {
        this.service = service;
    }

    @PostMapping
    public SavedVehicleResponse add(@Valid @RequestBody CreateSavedVehicleRequest request, @AuthenticationPrincipal Jwt jwt) {
        return SavedVehicleResponse.from(service.add(
                UUID.fromString(jwt.getSubject()),
                request.make(),
                request.model(),
                request.modelYear(),
                request.engine(),
                request.vin()
        ));
    }

    @GetMapping
    public List<SavedVehicleResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return service.list(UUID.fromString(jwt.getSubject())).stream().map(SavedVehicleResponse::from).toList();
    }

    @PutMapping("/{vehicleId}/primary")
    public void makePrimary(@PathVariable UUID vehicleId, @AuthenticationPrincipal Jwt jwt) {
        service.makePrimary(UUID.fromString(jwt.getSubject()), vehicleId);
    }
}
