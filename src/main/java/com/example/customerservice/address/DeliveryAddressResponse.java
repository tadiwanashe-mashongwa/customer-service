package com.example.customerservice.address;

public record DeliveryAddressResponse(String line1, String city, String country, String postalCode, boolean defaultAddress) {
    static DeliveryAddressResponse from(DeliveryAddress address) { return new DeliveryAddressResponse(address.getLine1(), address.getCity(), address.getCountry(), address.getPostalCode(), address.isDefaultAddress()); }
}
