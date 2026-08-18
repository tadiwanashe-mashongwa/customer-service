CREATE TABLE customer_profiles (
  id UUID PRIMARY KEY,
  keycloak_user_id UUID NOT NULL UNIQUE,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(320) NOT NULL,
  phone_number VARCHAR(30),
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  version BIGINT NOT NULL
);
