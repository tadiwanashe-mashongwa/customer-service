package com.example.customerservice.address;

import jakarta.validation.constraints.NotBlank;

public record CreateDeliveryAddressRequest(@NotBlank String line1, @NotBlank String city, @NotBlank String country, @NotBlank String postalCode) { }
