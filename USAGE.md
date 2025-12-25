# Usage Guide

Build and run
- Requirements: Java 17+, Maven 3.9+
- Build: mvn clean package
- Run: mvn spring-boot:run

Authentication
1) Get a token
   - POST http://localhost:8080/api/auth/login
   - Body: { "username": "user", "password": "password" }
   - Response: { "token": "<JWT>" }
2) Use header Authorization: Bearer <JWT>

Roles
- USER: can GET /api/books/**
- ADMIN: can POST/PUT/DELETE /api/books/** as well

Endpoints
- GET /api/books
- GET /api/books/{id}
- GET /api/books/isbn/{isbn}
- GET /api/books/large?size=1000&payloadKb=8
- POST /api/books
- PUT /api/books/{id}
- DELETE /api/books/{id}

Notes
- H2 console at /h2-console (JDBC: jdbc:h2:mem:cqsdb)
- Caching with Caffeine for all query endpoints
- Async event listeners log book create/update/delete events