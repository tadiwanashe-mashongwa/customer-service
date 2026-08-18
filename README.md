# SpareLink Customer Service

[![CI](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml/badge.svg)](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml)
[![Coverage](https://raw.githubusercontent.com/tadiwanashe-mashongwa/customer-service/main/.github/badges/jacoco.svg)](https://github.com/tadiwanashe-mashongwa/customer-service/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot 3.5.5](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)

Customer Service owns customer-facing data in the **SpareLink** automotive spare-parts platform: profiles, delivery addresses, and saved vehicles. Keycloak owns registration, credentials, roles, and access tokens. The service trusts a Keycloak JWT and derives the caller from its `sub` claim.

**Current JaCoCo instruction coverage: 87.9%.**

## Service boundary

| Concern | Owner |
| --- | --- |
| Login, registration, passwords, JWTs | Keycloak |
| Profiles, addresses, saved vehicles | Customer Service |
| Parts and prices | Catalogue Service |
| Stock | Inventory Service |
| Orders | Order Service |
| Payments | Payment Service |

```text
Browser / mobile client ── Bearer JWT ──> Customer Service :8085 ──> PostgreSQL
                                           ▲
                                           └── Keycloak issuer and signing keys
```

The customer ID is never accepted from request input. Address and vehicle selection/deletion are scoped to the authenticated JWT subject, so a customer cannot modify another customer's records.

## Capabilities

- Lazily creates a local profile for a valid Keycloak subject.
- Maintains customer profile details, delivery addresses, and saved vehicles.
- Allows one default delivery address and one primary vehicle per customer.
- Persists through PostgreSQL, JPA/Hibernate, and versioned Flyway migrations.
- Provides OpenAPI/Swagger, Actuator health, Docker, CI, JaCoCo and PostgreSQL Testcontainers tests.

## Stack

Java 21 · Spring Boot 3.5.5 · Maven · PostgreSQL · JPA/Hibernate · Flyway · Spring Security OAuth2 Resource Server · Keycloak · OpenAPI/Swagger · Testcontainers · JaCoCo · Docker · GitHub Actions

## API

Every `/api/**` endpoint requires `Authorization: Bearer <access-token>`.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/customers/me` | Read/create the caller profile. |
| `PUT` | `/api/customers/me` | Update profile details. |
| `POST`, `GET` | `/api/customers/me/addresses` | Add/list caller addresses. |
| `PUT` | `/api/customers/me/addresses/{addressId}/default` | Select default address. |
| `DELETE` | `/api/customers/me/addresses/{addressId}` | Delete owned address. |
| `POST`, `GET` | `/api/customers/me/vehicles` | Add/list caller vehicles. |
| `PUT` | `/api/customers/me/vehicles/{vehicleId}/primary` | Select primary vehicle. |
| `DELETE` | `/api/customers/me/vehicles/{vehicleId}` | Delete owned vehicle. |
| `GET` | `/actuator/health` | Health check. |

Example:

```http
POST /api/customers/me/vehicles
Authorization: Bearer <access-token>
Content-Type: application/json

{"make":"Toyota","model":"Corolla","modelYear":2020,"engine":"1.8L","vin":"JTDBR32E720000000"}
```

## Documentation and data model

| Resource | URL |
| --- | --- |
| Swagger UI | `http://localhost:8085/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8085/v3/api-docs` |
| Health | `http://localhost:8085/actuator/health` |

```text
customer_profiles 1 ─── * delivery_addresses
        │
        └────────────── * saved_vehicles
```

Flyway owns `customer_profiles`, `delivery_addresses`, and `saved_vehicles`. Hibernate uses `ddl-auto=validate`, so it validates rather than creates the schema.

## Run locally

Prerequisites: Java 21, Maven, PostgreSQL, and a Keycloak realm.

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/customer_db"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "your-password"
$env:KEYCLOAK_ISSUER_URI = "http://localhost:8080/realms/sparelink"
mvn spring-boot:run
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

## Quality gates

```powershell
mvn test
```

The TDD suite includes domain, service, controller, and PostgreSQL Testcontainers repository tests. JaCoCo reports are generated at `target/site/jacoco/`. GitHub Actions runs the full suite on each push and pull request, uploads coverage, and refreshes the badge on successful `main` builds.
