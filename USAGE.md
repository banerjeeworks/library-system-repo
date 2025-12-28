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
- GET /api/books/ids?ids=1,2,3  (Concurrent batch fetch)
- POST /api/books
- PUT /api/books/{id}
- DELETE /api/books/{id}

High-concurrency and streaming
- GET /api/books/stream (SSE): Subscribe to live Book events (use EventSource in browser)
  Example JS:
  const es = new EventSource('http://localhost:8080/api/books/stream', { withCredentials: false });
  es.onmessage = e => console.log('event:', e.data);

Metrics (ADMIN only)
- GET /api/metrics/executor
- GET /api/metrics/cache

Notes
- H2 console at /h2-console (JDBC: jdbc:h2:mem:cqsdb)
- Caching with Caffeine for all query endpoints
- Async event listeners log book create/update/delete events
- Correlation ID: every request includes/returns header X-Correlation-Id for traceability