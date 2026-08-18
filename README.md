# SpareLink Customer Service

[![CI](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml/badge.svg)](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml)
[![JaCoCo coverage](https://raw.githubusercontent.com/tadiwanashe-mashongwa/customer-service/main/.github/badges/jacoco.svg)](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml)

Customer Service owns the customer-facing data for the SpareLink automotive parts platform: a profile linked to a Keycloak identity, delivery addresses, and saved vehicles. It does not issue or store passwords; Keycloak is the identity provider.

## Technology

- Java 21, Spring Boot 3.5.5, Maven
- PostgreSQL, Spring Data JPA, Flyway
- Keycloak JWT resource server security
- Docker, Actuator
- JUnit, Mockito, PostgreSQL Testcontainers, JaCoCo

## Responsibilities

```text
Keycloak JWT (sub)
       |
       v
Customer profile <---- delivery addresses
       |
       +---- saved vehicles (one may be primary)
```

Every customer endpoint derives the customer identity from the JWT `sub` claim. Address and vehicle mutations are scoped to that authenticated customer; another customer cannot select or delete them.

## Run locally

Start PostgreSQL and create `customer_db`, then run:

```powershell
$env:KEYCLOAK_ISSUER_URI = "http://localhost:8080/realms/sparelink"
mvn spring-boot:run
```

The service listens on `http://localhost:8085`.

To run the complete test suite, including PostgreSQL Testcontainers tests:

```powershell
mvn test
```

## Run with Docker

```powershell
docker build -t sparelink/customer-service .
docker run --rm -p 8085:8085 `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/customer_db `
  -e SPRING_DATASOURCE_USERNAME=postgres `
  -e SPRING_DATASOURCE_PASSWORD=your-password `
  -e KEYCLOAK_ISSUER_URI=http://host.docker.internal:8080/realms/sparelink `
  sparelink/customer-service
```

## API

All `/api/**` endpoints require a Bearer JWT issued by Keycloak.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/customers/me` | Get or lazily create the caller's profile. |
| `PUT` | `/api/customers/me` | Update profile details. |
| `POST` | `/api/customers/me/addresses` | Add a delivery address. |
| `GET` | `/api/customers/me/addresses` | List delivery addresses. |
| `PUT` | `/api/customers/me/addresses/{addressId}/default` | Select the default address. |
| `DELETE` | `/api/customers/me/addresses/{addressId}` | Remove an address. |
| `POST` | `/api/customers/me/vehicles` | Add a saved vehicle. |
| `GET` | `/api/customers/me/vehicles` | List saved vehicles. |
| `PUT` | `/api/customers/me/vehicles/{vehicleId}/primary` | Select the primary vehicle. |
| `DELETE` | `/api/customers/me/vehicles/{vehicleId}` | Remove a saved vehicle. |
| `GET` | `/actuator/health` | Liveness and dependency health. |

Example request:

```http
POST /api/customers/me/vehicles
Authorization: Bearer <access-token>
Content-Type: application/json

{
  "make": "Toyota",
  "model": "Corolla",
  "modelYear": 2020,
  "engine": "1.8L",
  "vin": ""
}
```

## Database migrations

Flyway owns the schema. Current migrations create:

1. `customer_profiles`
2. `delivery_addresses`
3. `saved_vehicles`

Hibernate validates the schema rather than creating it (`spring.jpa.hibernate.ddl-auto=validate`).

## Testing strategy

- Domain and service tests cover customer ownership and primary/default selection rules.
- Controller tests verify the authenticated JWT subject is passed to the service layer.
- JPA repository tests run against real PostgreSQL through Testcontainers and execute Flyway migrations.
- GitHub Actions runs `mvn -B test`, uploads the JaCoCo report, and updates the coverage badge on `main`.
