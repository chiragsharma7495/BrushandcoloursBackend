# BrushAndColours - Spring Boot Backend

A RESTful backend for **BrushAndColours**, built with **Spring Boot** to manage creative activities and secure administrative operations. It includes user registration, JWT-based login, role-based access control, Activity CRUD, pagination and sorting, structured error responses, and service-layer unit testing.

> **Project status:** Core Activity and authentication APIs are implemented. Booking, customer event-location selection, admin maps, and Redis-based live tracking are ideas for future development - not current features.

## Tech stack

| Area | Technologies |
| --- | --- |
| Language & framework | Java, Spring Boot, Gradle |
| APIs & security | Spring Web, Spring Security, JWT (OAuth2 Resource Server), BCrypt |
| Database | MySQL, Spring Data JPA, Hibernate |
| Mapping & validation | MapStruct, Lombok, Jakarta Bean Validation |
| Testing | JUnit 5, Mockito |
| Cloud database | Azure Database for MySQL Flexible Server (development environment) |

## Features

- **Authentication:** Register users and log in to obtain a signed JWT; clients send it in the `Authorization: Bearer <token>` header.
- **Authorization:** New users receive the `USER` role. Activity creation, editing, and deletion are restricted to `ADMIN`.
- **Activity management:** Create, list, retrieve, update, and delete activities through REST endpoints.
- **DTO architecture:** Separate request/response DTOs from persistence entities; use MapStruct for mapping.
- **Pagination:** Retrieve activities in pages, with page metadata such as total elements and total pages.
- **Sorting:** Sort activities by supported fields in ascending or descending order.
- **Error handling:** Validation errors, missing resources, duplicate-email conflicts, unauthorized requests, and forbidden actions have appropriate HTTP responses.
- **Unit tests:** Test Activity service behavior with JUnit 5 and Mockito, including successful operations and not-found cases, without accessing a real database.

## Architecture

```text
Client / Postman
       |
       v
Spring Security
  |  JWT validation & role checks
  v
REST Controller  ---> Request DTO + validation
       |
       v
Service layer  <----> MapStruct mapper
       |
       v
Spring Data JPA Repository
       |
       v
Hibernate / MySQL
       |
       v
Response DTO ---> JSON response
```

### Authentication flow

```text
POST /api/auth/register
    -> validate input
    -> hash password with BCrypt
    -> save user with USER role

POST /api/auth/login
    -> AuthenticationManager checks credentials
    -> CustomUserDetailsService loads user from MySQL
    -> JWT is signed and returned

Protected request + Authorization: Bearer <token>
    -> Spring Security validates signature and expiry
    -> JWT roles are mapped to Spring authorities
    -> endpoint authorization rules are enforced
```

JWTs are **signed, not encrypted**. Do not place passwords or other secrets inside token claims.

## API endpoints

Base URL for local development: `http://localhost:8080`

### Authentication

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Public | Register a new `USER` |
| `POST` | `/api/auth/login` | Public | Authenticate and receive JWT |
| `GET` | `/api/auth/me` | Authenticated | Get authenticated user identity (where implemented) |

### Activities

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `GET` | `/api/activities` | Public | List activities with pagination/sorting |
| `GET` | `/api/activities/{id}` | Public | Retrieve an activity by ID |
| `POST` | `/api/activities` | `ADMIN` | Create an activity |
| `PUT` | `/api/activities/{id}` | `ADMIN` | Update an activity |
| `DELETE` | `/api/activities/{id}` | `ADMIN` | Delete an activity |

### Example: register

```http
POST /api/auth/register
Content-Type: application/json
```

```json
{
  "name": "Sample User",
  "email": "sample@example.com",
  "password": "ExamplePassword123!"
}
```

### Example: login

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "sample@example.com",
  "password": "ExamplePassword123!"
}
```

Example response (token shortened):

```json
{
  "token": "eyJ...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

For an authenticated request, provide:

```http
Authorization: Bearer <token>
```

**Security note:** Public registration cannot choose `ADMIN`. Administrator accounts must be provisioned or promoted through a trusted administrative process, not through a public registration request.

### Example: create activity (`ADMIN`)

```http
POST /api/activities
Authorization: Bearer <admin-token>
Content-Type: application/json
```

```json
{
  "title": "Painting Workshop",
  "description": "Beginner-friendly creative session",
  "basePrice": 500,
  "category": "Painting",
  "city": "Delhi",
  "active": true
}
```

### Pagination and sorting

```http
GET /api/activities?page=0&size=10&sortBy=createdAt&direction=desc
```

| Parameter | Default | Purpose |
| --- | --- | --- |
| `page` | `0` | Zero-based page number |
| `size` | `10` | Number of results per page (capped at 100) |
| `sortBy` | `createdAt` | Activity property to sort by |
| `direction` | `desc` | `asc` or `desc` |

Supported sort fields include `title`, `basePrice`, `category`, `city`, `createdAt`, and `updatedAt`. Invalid sort-field names fall back to `createdAt` in the current implementation.

## Getting started

### Prerequisites

- Compatible Java JDK for the project's configured Gradle/Spring Boot version
- MySQL database (local or hosted)
- Git
- IntelliJ IDEA or another Java IDE (optional)

The Gradle Wrapper is included, so a separate Gradle installation is not required.

### 1. Clone the repository

```bash
git clone https://github.com/chiragsharma7495/BrushandcoloursBackend.git
cd BrushandcoloursBackend
```

### 2. Configure the database

Create a MySQL database, for example:

```sql
CREATE DATABASE brushandcolours;
```

Use your local MySQL server or an authorized Azure MySQL connection. Ensure the database user has the appropriate privileges and that network/firewall settings permit access.

### 3. Set environment variables

The application expects these variables:

| Variable | Description |
| --- | --- |
| `DB_URL` | JDBC URL of your MySQL database |
| `DB_USERNAME` | MySQL user |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Base64-encoded random key of at least 32 bytes for HS256 |

Example **template only** (do not commit real credentials):

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-seconds=3600
```

Store the variables in IntelliJ's run configuration or your shell environment. **Never commit or share the generated secret.**

### 4. Run the application

On Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

On macOS/Linux:

```bash
./gradlew bootRun
```

Once the server starts, try:

```http
GET http://localhost:8080/api/activities?page=0&size=10
```

### 5. Run tests

Windows:

```powershell
.\gradlew.bat test
```

macOS/Linux:

```bash
./gradlew test
```

The Activity service unit tests use mocked repositories/mappers and do not need Azure MySQL. Other application-context or integration tests, if present, may have additional configuration requirements.

## Unit testing approach

Tests are kept under `src/test/` and use **JUnit 5 + Mockito**. Tests follow **Arrange -> Act -> Assert**, with Mockito's `when(...).thenReturn(...)` and `verify(...)` to configure and inspect dependency calls.

Coverage currently being developed includes:

- Fetch an existing activity
- Throw an exception when an activity is missing
- Create and save an activity
- Delete an existing activity
- Reject deletion of a missing activity
- Update, pagination, and sorting scenarios

## Project organization

```text
src/
├── main/
│   ├── groovy/
│   │   └── com/example/brushandcoloursBackend/
│   │       ├── activities/     # Activity controller, service, repository, DTOs, mapper
│   │       ├── auth/           # Registration, login, JWT service and DTOs
│   │       ├── security/       # Security configuration and authentication handling
│   │       └── exception/      # Application exceptions and error responses
│   └── resources/
│       └── application.properties
└── test/
    └── groovy/
        └── com/example/brushandcoloursBackend/
```

## Error responses

| Status | Meaning | Example |
| --- | --- | --- |
| `400` | Bad Request | Invalid input/validation |
| `401` | Unauthorized | Missing, invalid, or expired JWT |
| `403` | Forbidden | Authenticated `USER` attempts an `ADMIN` operation |
| `404` | Not Found | Unknown activity ID |
| `409` | Conflict | Email already registered |

Application errors are handled centrally using `@RestControllerAdvice`; security failures are handled by Spring Security's authentication entry point and access-denied handler.

## Configuration and security notes

- Keep **passwords, JWT signing keys, `.env` files, IDE settings, and private keys** out of Git.
- Do not expose an `ADMIN` role selector in public registration.
- Limit database network access to trusted clients and application servers.
- Apply HTTPS when serving the API publicly.
- JWT claims are readable by clients; never include secrets in them.
- Review the local development JPA schema setup before production use; plan schema migrations instead of relying on automatic schema updates.
- Deployment of the backend application to Azure App Service should not be assumed complete.

## Future improvements

These are **planned ideas**, not implemented capabilities:

- Customer event booking with dates, address, and location coordinates stored in MySQL
- Admin map with fixed event destination and the admin's current device location
- Route display and estimated arrival time
- Redis/WebSocket-based live crew tracking if multi-user monitoring becomes necessary
- Expanded unit, controller, authorization, and integration test coverage
- Production-ready database migrations and CI/CD

---

**BrushAndColours Backend** - a hands-on project focused on building secure, maintainable REST APIs using Spring Boot and modern backend development practices.
