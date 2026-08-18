package com.example.customerservice.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    List<DeliveryAddress> findByCustomerProfileId(UUID customerProfileId);
}
