package com.example.customerservice.address;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/customers/me/addresses")
public class DeliveryAddressController {
    private final DeliveryAddressService service;
    public DeliveryAddressController(DeliveryAddressService service) { this.service = service; }
    @PostMapping
    public DeliveryAddressResponse add(@Valid @RequestBody CreateDeliveryAddressRequest request, @AuthenticationPrincipal Jwt jwt) {
        return DeliveryAddressResponse.from(service.add(UUID.fromString(jwt.getSubject()), request.line1(), request.city(), request.country(), request.postalCode()));
    }
    @GetMapping
    public List<DeliveryAddressResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return service.list(UUID.fromString(jwt.getSubject())).stream().map(DeliveryAddressResponse::from).toList();
    }
    @PutMapping("/{addressId}/default")
    public void makeDefault(@PathVariable UUID addressId, @AuthenticationPrincipal Jwt jwt) {
        service.makeDefault(UUID.fromString(jwt.getSubject()), addressId);
    }

    @DeleteMapping("/{addressId}")
    public void remove(@PathVariable UUID addressId, @AuthenticationPrincipal Jwt jwt) {
        service.remove(UUID.fromString(jwt.getSubject()), addressId);
    }
}
