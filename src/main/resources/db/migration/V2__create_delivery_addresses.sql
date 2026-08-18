CREATE TABLE delivery_addresses (
  id UUID PRIMARY KEY,
  customer_profile_id UUID NOT NULL REFERENCES customer_profiles(id),
  line1 VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  country VARCHAR(100) NOT NULL,
  postal_code VARCHAR(30) NOT NULL,
  default_address BOOLEAN NOT NULL DEFAULT FALSE,
  version BIGINT NOT NULL
);
CREATE INDEX idx_delivery_addresses_customer_profile_id ON delivery_addresses(customer_profile_id);
