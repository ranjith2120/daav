# RuralNet Optimizer

RuralNet Optimizer is a planning application for weighted, undirected rural infrastructure networks. It provides persistent network data, minimum spanning forests, shortest routes, resilience analysis, and isolated failure scenarios.

## Implementation phases and structure

1. Bootstrap: React/Vite frontend, Spring Boot API, MySQL/Flyway configuration, health check, and local environment template.
2. Network management: persisted nodes, edges, revision snapshots, validation, CRUD, and demonstration dataset.
3. Analysis: deterministic Kruskal/Prim, Dijkstra, Tarjan, and saved result records.
4. Workflows: interactive explorer, routing/optimization, temporary simulations and dashboard/history.
5. Verification and delivery: automated coverage, responsive/accessibility work, performance profile, and deployment guide.

```text
backend/  Spring Boot API, services, graph algorithms, JPA repositories, Flyway migrations
frontend/ React + TypeScript + Vite application and feature modules
docker-compose.yml  Local MySQL 8 service
.env.example        Local configuration template (no credentials)
```

## Requirements

JDK 21, Maven 3.9+ (or Maven Wrapper), Node 20+, npm 10+, and MySQL 8+. Docker Compose can provide MySQL.

## Run locally

1. Copy `.env.example` to `.env` and set a local MySQL password. Docker Compose reads `.env`; Spring Boot does not load `.env` automatically, so also provide these values in the API process environment.
2. Start MySQL with `docker compose up -d mysql`.
3. From `backend`, start the API with `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). For PowerShell, set the database variables first:

   ```powershell
   $env:DB_HOST = "localhost"
   $env:DB_PORT = "3306"
   $env:DB_NAME = "ruralnet"
   $env:DB_USER = "ruralnet"
   $env:DB_PASSWORD = "change-me-locally"
   .\mvnw.cmd spring-boot:run
   ```

4. Start UI from `frontend`: `npm install` then `npm run dev`.
5. Visit `http://localhost:5173`; use **Load demonstration network** to initialize the synthetic graph.

Flyway owns schema changes; Hibernate schema auto-update is disabled. The API reads `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, and `CORS_ALLOWED_ORIGINS`. Swagger UI is available at `/swagger-ui/index.html`.

API base path: `/api/v1`. Health: `/actuator/health`. In-memory development is not supported; MySQL is required for persistence.

## Verification

From `frontend`, run `npm test` and `npm run build`. From `backend`, run `mvnw.cmd test` (or `./mvnw test`). The current backend tests exercise the graph algorithms and demonstration fixture. API persistence, Flyway migrations, and end-to-end workflows require a running MySQL 8 instance; use the Compose service for those checks.
