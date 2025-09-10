# Spring Boot Authentication API Checklist (Prioritized)

---

## Essentials (Minimum Viable Product)
These are the must-haves before you can call your API "production-ready".

### 1. Project Setup
- [x] Latest stable **Spring Boot version**
- [X] Dependencies: Spring Web, Spring Security, Spring Data JPA, Database driver, Lombok, Validation API
- [X] Environment variables for DB credentials, JWT secrets

### 2. Database & Entities
- [X] **User** and **Role** entities (with relationships)
- [X] Passwords hashed with **BCrypt**
- [X] Unique constraints on `username`/`email`
- [X] Initial script for roles (e.g., `ROLE_USER`, `ROLE_ADMIN`)

### 3. Authentication & Authorization
- [X] **User registration** endpoint (with validation)
- [X] **Login** endpoint (returns JWT or session)
- [X] **JWT-based authentication** (stateless)
- [X] Token expiration and validation
- [ ] Role-based access control (`@PreAuthorize`, `@Secured`)

### 4. Exception Handling & Responses
- [X] Global exception handler (`@ControllerAdvice`)
- [X] Consistent response DTOs for success & errors
- [X] Proper HTTP status codes

### 5. Security Best Practices
- [X] CSRF disabled (stateless REST API)
- [X] CORS configured properly
- [X] Sensitive info (passwords, tokens) never exposed
- [X] HTTPS required in production

### 6. Testing
- [ ] Unit tests for core services
- [ ] Integration tests for login/registration
- [ ] Manual testing with Postman/Insomnia

---

## Nice-to-Have Extras (Polish & Advanced Features)
These make your API more robust, scalable, and user-friendly, but are not strictly required for MVP.

### 7. User Management & Deletion
- [X] Account deletion request endpoint (`DELETE /request-delete`)
- [X] Email confirmation token for deletion
- [X] Token expiration check
- [X] Cascade-aware deletion of parent/child entities
- [ ] Global exception handling for deletion errors
- [ ] Optional soft delete flag or logging for audit

### 8. Documentation
- [ ] Swagger/OpenAPI docs
- [ ] README with setup instructions
- [ ] Exported Postman/Insomnia collection

### 9. Deployment & Monitoring
- [ ] Configurable DB (Postgres/MySQL for prod, H2 for dev/test)
- [X] Dockerfile & docker-compose
- [ ] Logging with Logback/Log4j
- [ ] Monitoring with Spring Actuator
- [ ] Error tracking (Sentry, ELK, etc.)

### 10. Advanced Security & UX
- [ ] Refresh tokens support
- [ ] Account lockout after repeated login failures
- [X] Email verification flow
- [ ] Forgot/reset password flow
- [ ] Multi-factor authentication (MFA)

### 11. Admin & Data Management
- [ ] Role/permission management endpoints
- [ ] Pagination & filtering for user lists

---