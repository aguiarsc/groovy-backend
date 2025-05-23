# Groovy Music Streaming Backend

<div align="center">

  ![Image](https://github.com/user-attachments/assets/27adf966-5e2f-4645-9f9a-ae003d7a0539)
  
  <h3>🎵 Robust REST API for the Groovy Music Streaming Platform 🎵</h3>
</div>

<p align="center">
  <a href="#overview">Overview</a> •
  <a href="#architecture">Architecture</a> •
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#api-documentation">API Documentation</a> •
  <a href="#data-model">Data Model</a> •
  <a href="#security">Security</a> •
  <a href="#code-organization">Code Organization</a> •
  <a href="#configuration">Configuration</a> •
</p>

---

## Overview

The Groovy Backend is built with Spring Boot and provides a comprehensive API for music streaming functionalities. It handles user authentication, music content management, playlist creation, favorites, and all other server-side operations for the Groovy platform.

### Core Capabilities

- **User Management**: Registration, authentication, and profile management
- **Content Management**: Music, albums, artists, and playlists
- **Media Streaming**: Audio file storage and streaming
- **Authorization**: Role-based access control (USER, ARTIST, ADMIN)
- **Data Persistence**: Efficient database operations and relationships
- **API Documentation**: Comprehensive Swagger/OpenAPI documentation

---

## Architecture

The backend follows a layered architecture pattern for clear separation of concerns:

```
                            ┌─────────────────────────────────────────────────────────────┐
                            │                     Controller Layer                        │
                            │                                                             │
                            │  Handles HTTP requests, input validation, and responses     │
                            └─────────────────────────────┬───────────────────────────────┘
                                                          │
                                                          ▼
                            ┌─────────────────────────────────────────────────────────────┐
                            │                      Service Layer                          │
                            │                                                             │
                            │  Implements business logic and orchestrates operations      │
                            └─────────────────────────────┬───────────────────────────────┘
                                                          │
                                                          ▼
                            ┌─────────────────────────────────────────────────────────────┐
                            │                  Repository/DAO Layer                       │
                            │                                                             │
                            │  Manages data access and persistence operations             │
                            └─────────────────────────────┬───────────────────────────────┘
                                                          │
                                                          ▼
                            ┌─────────────────────────────────────────────────────────────┐
                            │                       Data Layer                            │
                            │                                                             │
                            │  Database and external storage systems                      │
                            └─────────────────────────────────────────────────────────────┘
```

### Design Patterns

The backend implements several design patterns:

1. **Repository Pattern**: Abstracts data access logic
2. **DTO Pattern**: Separates data transfer objects from domain entities
3. **Service Layer Pattern**: Encapsulates business logic
4. **MVC Pattern**: Separates concerns for web handling
5. **Builder Pattern**: Used for object construction (via Lombok)
6. **Dependency Injection**: Spring-managed components
7. **Adapter Pattern**: For mapping between entities and DTOs

---

## Tech Stack

### Core Technologies
- **Java 17**: Programming language
- **Spring Boot 3.x**: Application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access
- **H2 Database**: In-memory database (configurable for production)
- **Maven**: Dependency management and build tool

### Libraries and Dependencies
- **JWT (jjwt)**: JSON Web Token implementation for authentication
- **Lombok**: Reduces boilerplate code
- **Springdoc OpenAPI**: API documentation
- **Jackson**: JSON serialization/deserialization
- **Commons IO**: File operations utilities
- **Hibernate Validator**: Input validation

### Development Tools
- **Spring DevTools**: Development productivity
- **H2 Console**: Database management interface
- **Actuator**: Application monitoring and management

---

### API Overview

| API Group      | Base Path      | Description                                 |
|----------------|---------------|----------------------------------------------|
| Authentication | `/api/auth`   | Authentication and user registration         |
| Users          | `/api/users`  | User profile management                      |
| Songs          | `/api/songs`  | Song operations and streaming                |
| Albums         | `/api/albums` | Album management                             |
| Artists        | `/api/artists`| Artist profiles and content                  |
| Playlists      | `/api/playlists` | Playlist operations                       |
| Favorites      | `/api/favorites` | User favorites management                 |
| Files          | `/api/files`  | File upload and media management             |
| Health         | `/api/health` | System health check                          |

### Authentication Endpoints

| Method | Endpoint            | Description              | Auth Required|
|--------|---------------------|--------------------------|--------------|
| POST   | `/api/auth/register`| Register new user        | No           |
| POST   | `/api/auth/login`   | Authenticate user        | No           |

### User Endpoints

| Method | Endpoint                | Description               | Auth Required|
|--------|-------------------------|---------------------------|--------------|
| GET    | `/api/users/current`    | Get current user details  | Yes          |
| GET    | `/api/users/{id}`       | Get user by ID            | Yes          |
| PUT    | `/api/users/{id}`       | Update user profile       | Yes          |

### Song Endpoints

| Method | Endpoint                   | Description              | Auth Required |
|--------|-----------------------------|--------------------------|--------------|
| GET    | `/api/songs`                | Get all songs            | Yes          |
| GET    | `/api/songs/{id}`           | Get song by ID           | Yes          |
| GET    | `/api/songs/{id}/stream`    | Stream song audio        | No           |
| POST   | `/api/songs`                | Create new song          | Yes (ARTIST) |
| PUT    | `/api/songs/{id}`           | Update song              | Yes (ARTIST) |
| DELETE | `/api/songs/{id}`           | Delete song              | Yes (ARTIST) |

For a complete list of endpoints and details, refer to the Swagger documentation.

---

## Data Model

The backend implements a relational data model with the following key entities:

### Core Entities

#### User
```
- id: Long (PK)
- name: String
- email: String (unique)
- password: String (encrypted)
- role: Role (enum: USER, ARTIST, ADMIN)
```

#### Artist (extends User)
```
- biography: String
- profilePicture: String
```

#### Album
```
- id: Long (PK)
- name: String
- coverImage: String
- artist: Artist (FK)
```

#### Song
```
- id: Long (PK)
- title: String
- duration: Double
- filePath: String
- album: Album (FK)
- artist: Artist (FK)
```

#### Playlist
```
- id: Long (PK)
- name: String
- user: User (FK)
- songs: List<Song> (Many-to-Many)
```

#### UserFavorite
```
- id: Long (PK)
- user: User (FK)
- song: Song (FK)
- createdAt: LocalDateTime
```

---

## Security

### Authentication Flow

1. **Registration**: Users register with email/password, creating a new account
2. **Login**: Valid credentials generate a JWT token
3. **Token Usage**: Subsequent requests include the token in the Authorization header
4. **Validation**: Backend validates the token for each secured endpoint
5. **Expiration**: Tokens expire after a configured time period

### Security Components

#### JwtService
Handles token generation, validation, and extraction of claims.

```java
public class JwtService {
    // Token operations (generation, validation, extraction)
}
```

#### JwtAuthenticationFilter
Intercepts requests to extract and validate JWT tokens.

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Request filtering and authentication
}
```

#### SecurityConfig
Configures security policies, authentication providers, and endpoint access rules.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Security configuration
}
```

### Role-Based Authorization

The system implements three primary roles:

1. **USER**: Regular users with basic music listening capabilities
2. **ARTIST**: Musicians who can upload and manage their content
3. **ADMIN**: Administrators with full system access

Authorization is enforced at the controller level using Spring Security annotations:

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public ResponseEntity<?> getAllUsers() {
    // Admin-only operation
}
```

## Code Organization

The backend follows a standard Spring Boot project structure:

```
src/
├── main/
│   ├── java/com/daw/groovy/
│   │   ├── config/        # Configuration classes
│   │   ├── controller/    # REST controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── entity/        # JPA entities
│   │   ├── enums/         # Enumerations
│   │   ├── exception/     # Custom exceptions
│   │   ├── mapper/        # Entity-DTO mappers
│   │   ├── repository/    # Data repositories
│   │   ├── security/      # Security components
│   │   ├── service/       # Business logic
│   │   └── storage/       # File storage
│   │
│   └── resources/
│       ├── application.properties  # Main configuration
│       ├── application-dev.properties  # Development profile
│       ├── application-prod.properties # Production profile
│       └── data.sql                # Initial data script
│
└── test/                  # Test classes
    └── java/com/daw/groovy/
        ├── controller/    # Controller tests
        ├── repository/    # Repository tests
        └── service/       # Service tests
```

### Key Components

#### Controllers
Handle HTTP requests and responses, input validation, and basic error handling.

```java
@RestController
@RequestMapping("/api/songs")
public class SongController {
    // HTTP endpoints for song operations
}
```

#### Services
Implement business logic and orchestrate operations.

```java
@Service
public class SongService {
    // Business logic for song management
}
```

#### Repositories
Provide data access and persistence operations.

```java
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Data access methods
}
```

#### DTOs
Transport data between layers with appropriate validation.

```java
public class SongDto {
    // Data transfer properties and validation
}
```

#### Entities
Represent the domain model and database schema.

```java
@Entity
@Table(name = "songs")
public class Song {
    // Entity properties and relationships
}
```

#### Mappers
Convert between entity and DTO objects.

```java
@Component
public class SongMapper {
    // Mapping methods between Song and SongDto
}
```

---

## Configuration

### Application Properties

The main configuration is in `application.properties`:

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/

# Database configuration
spring.datasource.url=...
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=...
spring.datasource.password=...
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File storage
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# JWT Configuration
jwt.secret=...
jwt.expiration=...

# Logging
logging.level.org.springframework=INFO
logging.level.com.daw.groovy=DEBUG
```

### Bean Configuration

Java-based configuration is used for complex bean setup:

```java
@Configuration
public class AppConfig {
    // Bean definitions
}
```

---

### Coding Standards

- Follow Google Java Style Guide
- Use clear, descriptive names
- Write comprehensive Javadoc comments
- Maintain test coverage for all code

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

<div align="center">
  <p>🎵 Developed as a final grade project for DAW course 🎵</p>
</div>
