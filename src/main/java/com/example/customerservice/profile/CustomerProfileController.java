package com.example.customerservice.profile;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerProfileController {
    private final CustomerProfileService service;

    public CustomerProfileController(CustomerProfileService service) { this.service = service; }

    @GetMapping("/me")
    public CustomerProfileResponse me(@AuthenticationPrincipal Jwt jwt) {
        CustomerProfile profile = service.getOrCreate(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                jwt.getClaimAsString("email"),
                null
        );
        return CustomerProfileResponse.from(profile);
    }

    @PutMapping("/me")
    public CustomerProfileResponse update(@Valid @RequestBody UpdateCustomerProfileRequest request, @AuthenticationPrincipal Jwt jwt) {
        return CustomerProfileResponse.from(service.update(UUID.fromString(jwt.getSubject()), request));
    }
}
