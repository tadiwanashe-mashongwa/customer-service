package com.example.customerservice.vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.util.UUID;

@Entity
@Table(name = "saved_vehicles")
public class SavedVehicle {
    @Id private UUID id;
    private UUID customerProfileId;
    private String make;
    private String model;
    private int modelYear;
    private String engine;
    private String vin;
    private boolean primaryVehicle;
    @Version private long version;

    protected SavedVehicle() { }

    private SavedVehicle(UUID customerProfileId, String make, String model, int modelYear, String engine, String vin) {
        this.id = UUID.randomUUID();
        this.customerProfileId = customerProfileId;
        this.make = make;
        this.model = model;
        this.modelYear = modelYear;
        this.engine = engine;
        this.vin = vin;
    }

    public static SavedVehicle create(UUID customerProfileId, String make, String model, int modelYear, String engine, String vin) {
        return new SavedVehicle(customerProfileId, make, model, modelYear, engine, vin);
    }

    public UUID getCustomerProfileId() { return customerProfileId; }
    public UUID getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getModelYear() { return modelYear; }
    public String getEngine() { return engine; }
    public String getVin() { return vin; }
    public boolean isPrimaryVehicle() { return primaryVehicle; }
}
