# RuralNet Optimizer — Product Requirements Document

## 1. Product Overview

RuralNet Optimizer is a desktop-first web application for planning and analyzing infrastructure networks that connect rural villages, hospitals, schools, warehouses, water facilities, power stations, and distribution centers. It combines a visual graph editor with accurate graph algorithms to answer three operational questions: what is the least-cost physical network, how should goods or services travel between locations, and what happens when critical infrastructure fails.

The application persists baseline networks and analytical history in MySQL. Temporary failures are isolated in simulations so a user can compare scenarios without changing the saved baseline.

## 2. Problem Statement

Planning rural roads, pipes, power lines, or distribution links requires balancing capital cost, service reach, and resilience. Spreadsheets and static maps do not reliably identify a minimum-cost network, shortest delivery route, or single points of failure. RuralNet Optimizer provides a shared source of truth and repeatable calculations derived entirely from stored network data.

## 3. Goals and Non-Goals

### Goals

- Model weighted, undirected infrastructure networks with typed locations and operational links.
- Produce minimum spanning trees (MSTs) or forests using Kruskal and Prim, including exact totals and selected edges.
- Plan reachable shortest routes with Dijkstra using either distance or monetary cost.
- Identify bridges and articulation points, and evaluate one or many temporary edge failures.
- Make all results inspectable in a responsive, accessible visual interface.
- Persist auditable optimization runs and simulation summaries.

### Non-Goals

- Real-time IoT telemetry, traffic prediction, geocoding, or GIS routing.
- Capacity-constrained flow optimization, directed networks, or negative-weight routing in the first release.
- Authentication/authorization beyond deployment integration points.
- Automatic construction scheduling, procurement, or financial forecasting.

## 4. Target Users and User Stories

| User | Story |
| --- | --- |
| Rural infrastructure planner | I can create locations and proposed links, then select the least-cost network that reaches every reachable location. |
| Distribution coordinator | I can select a source, destination, and metric to see the shortest route, its stops, and its total. |
| Resilience analyst | I can find critical locations/links and test multiple failures without overwriting the approved network. |
| Program manager | I can review current costs, connectivity, categories, historical runs, and simulation outcomes. |
| Data administrator | I can import, reset, validate, and correct demonstration or operational network data. |

## 5. Functional Requirements

### 5.1 Network management

- A node has a generated UUID, unique `code`, name, type, coordinates, non-negative demand and supply, description, version, and timestamps. Types are `VILLAGE`, `HOSPITAL`, `SCHOOL`, `WAREHOUSE`, `WATER_FACILITY`, `POWER_STATION`, and `DISTRIBUTION_CENTER`.
- An edge has a UUID, two different endpoint node IDs, positive distance, non-negative construction cost, non-negative capacity, operational status (`ACTIVE`, `DISABLED`, `PLANNED`), version, and timestamps.
- Edges are undirected. The pair of endpoint IDs is canonicalized (`minId`, `maxId`) and unique; self-loops and duplicate pairs are rejected.
- Users can create, inspect, update, and delete nodes/edges. Deleting a node requires confirmation and deletes its incident edges atomically. A conflicted update returns the current version.
- The network view returns nodes, edges, connected components, and validation warnings for the active baseline or a supplied simulation.

### 5.2 Visualization

- React Flow renders nodes by type and active/failed state, and renders edge labels with distance and cost.
- Users can pan, zoom, fit view, select nodes/edges, drag node positions, and open an accessible details panel. Drag completion persists coordinates only after user confirmation or explicit save.
- Overlay styles distinguish optimized MST edges, chosen route edges, bridge edges, disabled edges, selected elements, and failure impact. A legend and graph controls are always available.

### 5.3 Optimization and routing

- Users choose Kruskal or Prim and an edge weighting metric for MST. Release 1 supports `COST` only; the API reserves `DISTANCE` for future use and rejects it with a documented 422 until enabled.
- A run returns a minimum spanning forest for all active nodes/edges, including component count, edge IDs, total cost, per-component totals, isolated nodes, execution time, and deterministic algorithm details.
- Route planning accepts active source/destination IDs and `DISTANCE` or `COST`. It returns a node sequence, edge sequence, metric total, both physical distance and construction-cost totals, and ordered stops. Unreachable destinations return a successful `reachable: false` response rather than an invented route.

### 5.4 Fault tolerance and simulation

- Analysis returns active-graph bridges and articulation points plus the components affected by removal.
- A simulation starts from a baseline revision and maintains a set of disabled edge IDs. It may include multiple failures, independently restore any edge, reset all failures, and be saved as a history record.
- Every simulation result recalculates components, isolated nodes, bridges/articulation points, Kruskal forest, and optional routes. The baseline network is never mutated by these operations.
- Before/after comparison reports component count, affected node IDs, baseline/failed MST costs, lost routes, alternative routes when they exist, and extra connection cost only when both compared forests span the same component scope.

### 5.5 Dashboard and history

- Dashboard data is calculated server-side from the requested baseline or simulation: node/edge counts, total active edge cost, MST/forest cost, components, isolated nodes, bridge count, node types, cost bins, and resilience comparison.
- Optimization history stores algorithm, input revision, selected edge snapshot, results, requester-provided name/notes, timestamps, and a result schema version.
- Simulation history stores baseline revision, disabled-edge snapshot, summary/result JSON, name/notes, timestamps, and a result schema version.
- Sample data initialization is idempotent; reset requires explicit confirmation and replaces only data marked as sample.

## 6. Non-Functional Requirements

- Support 2,000 nodes and 10,000 edges per network; algorithm requests complete within 5 seconds at this size on the deployment target, excluding database connection startup.
- Use UTC ISO-8601 timestamps, decimal monetary values (never floating-point currency), and JSON UTF-8.
- Maintain keyboard operability, visible focus, semantic labels, 4.5:1 text contrast, and status announcements for async operations.
- Target Chrome, Edge, and Firefox current/previous releases; responsive layout supports widths from 375px upward.
- APIs have p95 response time below 500 ms for normal CRUD/network retrieval and 5% maximum server-error rate per rolling day, excluding client/network faults.

## 7. Technology Stack and Architecture

Frontend: React 18+, TypeScript, Vite, Tailwind CSS, shadcn/ui, React Flow, Recharts, TanStack Query, React Router, and a small local UI store (Zustand or Context). Backend: Java 21+, Spring Boot 3.x, Spring Web, Spring Data JPA, Bean Validation, Flyway, Maven, and MySQL 8.0+. Tests use JUnit 5/Mockito, Spring Boot/Testcontainers integration tests, and Vitest/React Testing Library.

The backend uses Controllers → Services → Repositories → MySQL. DTOs isolate REST payloads from JPA entities. `graph` services receive a `GraphSnapshot` (immutable node/edge records) and return result DTOs; they must not use controllers, repositories, or JPA entities. A global exception handler produces the error envelope. A `NetworkSnapshotFactory` applies simulation disabled IDs over a baseline before algorithms run.

## 8. Application Pages and User Flows

| Page | Primary actions |
| --- | --- |
| Dashboard | Review derived KPIs/charts; open critical links, latest analyses, or an affected node. |
| Network Explorer | Navigate graph; inspect or select; filter by type/status; create a route/optimization from selection. |
| Minimum-Cost Optimization | Select algorithm; run, compare, inspect forest/components, and save a run. |
| Distribution Route Planner | Select source/destination/metric; run; inspect route or unreachable explanation. |
| Fault-Tolerance Simulator | Review criticality; disable/restore edges; compare baseline; run route/forest; save/reset scenario. |
| Infrastructure Management | Table/form CRUD, import sample data, validation feedback, destructive confirmations. |
| History | Filter and inspect immutable optimization and simulation records. |
| Settings & Algorithms | API endpoint preference, sample-data reset, metric definitions, complexity/help. |

On first visit, Dashboard offers “Load demonstration network.” A user can then explore, optimize, plan a route, create a simulation, and save results. Persistent network edits invalidate neither historical records nor their snapshots; the UI labels historical results with their source revision.

## 9. Graph Algorithms and Mathematical Definitions

Let active graph `G=(V,E)` be undirected. Each edge `e` has distance `d(e)>0` and cost `c(e)>=0`. Nodes with `ACTIVE`/`PLANNED` semantics are included only as configured; release 1 calculations use active edges and all nodes.

- **Kruskal:** sort active edges by `(cost, endpoint-low UUID, endpoint-high UUID)` and use union-find. Add an edge iff it joins two distinct sets. Time `O(E log E)`, space `O(V)`.
- **Prim:** process nodes in UUID order; start a new component at each unvisited node. Use a min-priority queue keyed by the same deterministic tuple. Time `O(E log V)`, space `O(V+E)`.
- **Minimum spanning forest:** for each connected component `C`, find a tree with `|C|-1` edges minimizing `Σ c(e)`. Forest cost is the sum across components. Kruskal and Prim must have equal total cost; selected IDs may differ only in equal-cost ties.
- **Dijkstra:** for metric `w(e)` of distance or cost, initialize source `0`, relax `dist[v] > dist[u]+w(u,v)` using a min-heap, and reconstruct from predecessors. It accepts only `w(e)>=0`. Time `O((V+E) log V)`.
- **Bridge:** edge `u-v` is critical if removing it increases connected components. **Articulation point:** vertex whose removal increases components. Use Tarjan DFS `disc[]`, `low[]`, parent tracking. Time `O(V+E)`.
- All totals are computed with `BigDecimal` for cost and `double`/`BigDecimal` consistently for distance (chosen convention documented in code); no rounded display value may be used as an algorithm input.

## 10. Backend REST API Specification

Base path: `/api/v1`. Successful responses use `{ "data": ..., "meta": { "requestId": "..." } }`; lists put pagination in `meta`. Errors use `{ "error": { "code": "VALIDATION_ERROR", "message": "...", "details": [{ "field": "name", "message": "must not be blank" }], "requestId": "..." } }`.

| Method/path | Request | Success | Failure behavior |
| --- | --- | --- | --- |
| `GET /nodes` | filters `type`, `q`, pagination | 200 node page | 400 invalid query |
| `POST /nodes` | node create DTO | 201 node | 400 validation, 409 duplicate code |
| `GET/PATCH/DELETE /nodes/{id}` | PATCH includes `version` | 200 / 204 | 404 missing, 409 stale version; DELETE returns 409 unless `cascade=true` |
| `GET /edges`, `POST /edges` | edge filters/create DTO | 200 / 201 | 400 invalid weight, 404 endpoint, 409 duplicate pair |
| `GET/PATCH/DELETE /edges/{id}` | PATCH includes version | 200 / 204 | 404/409 as above |
| `GET /network` | `simulationId?` | 200 graph/components/warnings | 404 simulation |
| `POST /network/validate` | optional `{simulationId}` | 200 validation report | 422 invalid topology references |
| `POST /optimizations` | `{algorithm:"KRUSKAL", metric:"COST", simulationId?, save?:false, name?, notes?}` | 200 result or 201 saved result | 422 unsupported metric/empty graph |
| `POST /routes/shortest-path` | `{sourceNodeId,destinationNodeId,metric,simulationId?}` | 200 route result | 404 nodes/simulation, 422 same/invalid state |
| `GET /resilience/criticality` | `simulationId?` | 200 bridges/articulation points | 404 simulation |
| `POST /simulations` | `{name?, disabledEdgeIds:[]}` | 201 scenario/result | 404 edge, 409 baseline revision changed |
| `PATCH /simulations/{id}` | `{disabledEdgeIds, version}` | 200 recalculated scenario | 409 stale version |
| `POST /simulations/{id}/restore` | `{edgeIds?:[]}` | 200 scenario | 404 IDs |
| `POST /simulations/{id}/reset` | none | 200 restored scenario | 404 ID |
| `POST /simulations/{id}/save` | `{name,notes?}` | 201 history record | 422 name invalid |
| `GET /dashboard` | `simulationId?` | 200 analytics | 404 simulation |
| `GET /history/optimizations`, `GET /history/simulations` | paging/filtering | 200 pages | 400 query |
| `POST /sample-data/initialize` | none | 201 summary | 409 already initialized unless `force=true` |
| `POST /sample-data/reset` | `{confirmation:"RESET_SAMPLE_DATA"}` | 200 summary | 422 confirmation mismatch |

Example node request: `{ "code":"VIL-001", "name":"Asha Village", "type":"VILLAGE", "latitude":18.5204, "longitude":73.8567, "demand":120.00, "supply":0 }`. Example edge request: `{ "nodeAId":"uuid", "nodeBId":"uuid", "distanceKm":12.4, "constructionCost":450000.00, "capacity":80.0, "status":"ACTIVE" }`.

## 11. Database Schema

`infrastructure_nodes(id BINARY(16) PK, code VARCHAR(40) UNIQUE NOT NULL, name VARCHAR(160) NOT NULL, type ENUM NOT NULL, latitude DECIMAL(9,6) NOT NULL, longitude DECIMAL(9,6) NOT NULL, demand DECIMAL(14,2) NOT NULL DEFAULT 0, supply DECIMAL(14,2) NOT NULL DEFAULT 0, description TEXT NULL, sample_data BOOLEAN NOT NULL DEFAULT FALSE, version BIGINT NOT NULL, created_at DATETIME(6), updated_at DATETIME(6))`. Index `(type)`, `(sample_data)`, and coordinate index as needed.

`infrastructure_edges(id BINARY(16) PK, node_a_id BINARY(16) NOT NULL FK nodes, node_b_id BINARY(16) NOT NULL FK nodes, endpoint_low_id BINARY(16) NOT NULL, endpoint_high_id BINARY(16) NOT NULL, distance_km DECIMAL(12,3) NOT NULL CHECK > 0, construction_cost DECIMAL(16,2) NOT NULL CHECK >= 0, capacity DECIMAL(14,2) NOT NULL CHECK >= 0, status ENUM NOT NULL, sample_data BOOLEAN NOT NULL, version BIGINT NOT NULL, created_at, updated_at, UNIQUE(endpoint_low_id,endpoint_high_id))`. Index both endpoint foreign keys and `(status)`.

`network_revisions(id BINARY(16) PK, revision_number BIGINT UNIQUE, created_at, reason)` records a stable baseline identity. `optimization_runs(id BINARY(16) PK, network_revision_id FK, algorithm ENUM, metric ENUM, selected_edges_json JSON, result_json JSON, name, notes, result_schema_version INT, created_at)` has indexes on revision/date/algorithm. `simulation_records(id BINARY(16) PK, network_revision_id FK, disabled_edge_ids_json JSON, result_json JSON, name, notes, result_schema_version INT, created_at, updated_at)` has indexes on revision/date. Mutable in-progress simulations can use `active_simulations` with `version`; saved records are immutable.

A revision is created transactionally for each successful baseline edit. Simulation calculations load the indicated revision’s node/edge snapshot and subtract `disabled_edge_ids`; they never update edge status. If full historical reconstruction is required, create `network_revision_nodes` and `network_revision_edges` snapshot tables; release 1 must use these tables for any saved record, not merely live IDs.

## 12. Frontend Component Architecture

- `AppShell`, `SidebarNav`, `TopBar`, `ErrorBoundary`, `Toaster`, and route-level loading/empty/error states.
- `NetworkCanvas` wraps React Flow; `NodeRenderer`, `EdgeRenderer`, `GraphLegend`, `GraphControls`, `SelectionDetails`, and `GraphFilters` receive view state and result overlays.
- Feature modules own query hooks and DTO mappers: `network`, `optimization`, `routing`, `resilience`, `simulations`, `dashboard`, and `history`.
- Forms use React Hook Form plus Zod schemas mirrored from API constraints; mutation success invalidates exact TanStack Query keys.
- Recharts uses only Dashboard API series. Components must show “No data” rather than zero/fabricated charts when a series is unavailable.

## 13. Data Validation and Error Handling

- Validate required strings, enum values, UUIDs, coordinate ranges (`latitude -90..90`, `longitude -180..180`), finite numerics, `demand/supply/cost/capacity >= 0`, and `distanceKm > 0` at client and server.
- Reject same endpoints, unknown/disabled routing endpoints, duplicate undirected pairs, negative Dijkstra weights, empty optimization graphs, and missing simulation edges with stable 4xx codes.
- Map `MethodArgumentNotValidException`, domain validation, entity-not-found, optimistic-lock, data-integrity, and unexpected exceptions centrally. Log unexpected exceptions with request ID; return only a generic 500 message.
- UI preserves form data on validation errors, maps field errors inline, provides retry on transient errors, and uses destructive confirmations for node/edge deletion and sample reset.

## 14. Security and Reliability

- Read MySQL credentials from `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`; never commit secrets. Provide `.env.example` only.
- Allow development CORS origins from `CORS_ALLOWED_ORIGINS`; production uses explicit HTTPS origins, least-privilege database access, TLS, and a reverse proxy.
- Use Spring Data parameter binding, Bean Validation, UUID validation, payload size limits, structured logs, request IDs, actuator health endpoints, and database backups/migrations.
- Use optimistic locking on mutable nodes, edges, and active simulations. Return 409 with a refresh instruction for concurrent writes.

## 15. Testing Strategy

- Unit-test each graph algorithm for connected/disconnected graphs, ties, zero costs, isolated nodes, bridge/articulation cases, repeatability, and no mutation of input snapshots.
- Unit-test services/controllers with JUnit 5, Mockito, and MockMvc for status/envelopes/validation/locking.
- Run MySQL integration tests with Testcontainers for migrations, constraints, repository behavior, and key API workflows.
- Frontend component tests cover controls, canvas overlays, forms, accessibility keyboard use, loading/error/empty states, and API result rendering. Add API contract tests and a small Playwright end-to-end path: initialize sample → optimize → route → fail edge → compare → reset.
- CI runs formatting, typecheck, unit tests, integration tests, and a production frontend build. Algorithm result fixtures include exact values and edge IDs.

## 16. Sample Dataset

Seed at least 14 nodes: 5 villages, 2 hospitals, 2 schools, 2 water facilities, 1 power station, 1 warehouse, and 1 distribution center. Include active links forming a main connected component with at least two equal-cost MST alternatives, an isolated/second component, a bridge to a remote clinic/village, a cycle offering an alternative route, and a planned/disabled edge excluded from active analysis. Distances, costs, capacity, demand, and supply are all realistic non-zero values except legitimate supply/demand zeros. Seed assertions document expected components, one known bridge, one articulation point, a reachable route, and an unreachable route.

## 17. Acceptance Criteria

- CRUD persists valid data, rejects invalid/duplicate topology with documented error envelopes, and updates canvas/table state.
- For every seeded and test graph, Kruskal and Prim return the same forest cost, `|V|-components` selected edges, and costs calculated from returned selected edges.
- Dijkstra returns the correct metric-minimal path and totals; unreachable pairs return `reachable:false`, empty path, and a clear explanation.
- Tarjan results match fixture bridges/articulation points; disabling any returned bridge increases component count when its endpoint component has more than one node.
- Simulated failures alter only the simulation snapshot; reset restores baseline calculation results byte-for-byte apart from timestamps/request metadata.
- Dashboard metrics/charts equal data independently recomputed from the API graph/simulation payload; no hardcoded values are present.
- Keyboard-only users can navigate all pages, invoke controls, inspect results, and receive async status messages.
- API integration suite and frontend tests pass; 2k-node/10k-edge performance target is measured and met in CI’s performance profile.

## 18. Implementation Phases

1. Bootstrap repositories, Vite/Spring apps, Flyway/MySQL configuration, CI, API envelope, health check, and sample seed.
2. Deliver node/edge schema, CRUD, validation, optimistic locking, management UI, and graph explorer.
3. Implement immutable graph snapshots, Kruskal/Prim, optimization endpoint/history, overlays, and comparison UI.
4. Implement Dijkstra endpoint/planner, unreachable states, and route overlays.
5. Implement Tarjan, simulation lifecycle, baseline comparison, resilience UI, and saved scenarios.
6. Add dashboard/history, accessibility/performance hardening, test coverage, deployment documentation, and release validation.

## 19. Setup and Deployment Requirements

- Prerequisites: JDK 21, Maven 3.9+, Node 20+, npm 10+, and MySQL 8.0+. `backend` uses Maven wrapper; `frontend` uses npm lockfile.
- Local configuration supplies database and CORS variables. Flyway runs automatically; provide an explicit seed command/API restricted to development.
- Docker Compose supplies MySQL locally. Production uses externally managed MySQL, configured backups, environment/secret injection, HTTPS termination, health checks, and separate frontend static hosting or reverse proxy.
- Publish OpenAPI documentation at `/swagger-ui` in non-production or behind appropriate access control. Version migrations; never use JPA schema auto-update in production.

## 20. Assumptions and Open Questions

- The initial release is a single organization with no end-user identity model; authorization must be added before exposing production write APIs.
- “Cost” represents one-time construction cost, while “distance” represents route length; operating cost is out of scope.
- All physical links are treated as symmetric. Directed and capacity-aware routing need a future graph model change.
- Confirm whether `PLANNED` edges should participate in a separate proposed-network scenario or remain excluded in release 1.
- Confirm coordinate reference requirements (plain lat/lon visualization vs. a GIS basemap) and retention/audit requirements for operational data.
