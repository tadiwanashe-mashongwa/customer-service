CREATE TABLE saved_vehicles (
  id UUID PRIMARY KEY,
  customer_profile_id UUID NOT NULL REFERENCES customer_profiles(id),
  make VARCHAR(100) NOT NULL,
  model VARCHAR(100) NOT NULL,
  model_year INTEGER NOT NULL,
  engine VARCHAR(100),
  vin VARCHAR(17),
  primary_vehicle BOOLEAN NOT NULL DEFAULT FALSE,
  version BIGINT NOT NULL
);

CREATE INDEX idx_saved_vehicles_customer_profile_id ON saved_vehicles(customer_profile_id);
