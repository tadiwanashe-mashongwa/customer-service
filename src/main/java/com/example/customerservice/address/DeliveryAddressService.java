package com.example.customerservice.address;

import com.example.customerservice.profile.CustomerProfile;
import com.example.customerservice.profile.CustomerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class DeliveryAddressService {
    private final CustomerProfileRepository profiles;
    private final DeliveryAddressRepository addresses;

    public DeliveryAddressService(CustomerProfileRepository profiles, DeliveryAddressRepository addresses) {
        this.profiles = profiles;
        this.addresses = addresses;
    }

    public DeliveryAddress add(UUID keycloakUserId, String line1, String city, String country, String postalCode) {
        CustomerProfile profile = profiles.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Customer profile not found"));
        return addresses.save(DeliveryAddress.create(profile.getId(), line1, city, country, postalCode));
    }

    public List<DeliveryAddress> list(UUID keycloakUserId) {
        CustomerProfile profile = profiles.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Customer profile not found"));
        return addresses.findByCustomerProfileId(profile.getId());
    }

    public void makeDefault(UUID keycloakUserId, UUID addressId) {
        List<DeliveryAddress> customerAddresses = list(keycloakUserId);
        DeliveryAddress selected = customerAddresses.stream().filter(address -> address.getId().equals(addressId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Delivery address not found"));
        customerAddresses.forEach(DeliveryAddress::clearDefault);
        selected.makeDefault();
        addresses.saveAll(customerAddresses);
    }
}
