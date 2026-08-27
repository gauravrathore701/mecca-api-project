# api-nexus

Middleware connector that proxies requests to downstream APIs via a strict pipeline:
**Controller → Service → Manager → Adapter**

Each domain (auth, notification) is fully self-contained with its own package tree.

## Stack

- Java 21 LTS
- Spring Boot 3.3.5 (Tomcat embedded)
- Spring WebFlux (WebClient for downstream HTTP calls)
- Lombok
- Maven 3.9.9

## Port

Default: **4181** (`PORT` env var overrides)

All routes are served under the context path **`/api`**, set by
`SERVER_SERVLET_CONTEXT_PATH` in the systemd unit (not in
`application.properties`) — e.g. `POST /api/auth/login`.

## Architecture

```
controller  ← receives DTO (request contract from caller)
    ↓
service     ← translates DTO → Entity (internal contract)
    ↓
manager     ← orchestration layer, calls adapter
    ↓
adapter     ← translates Entity → Proxy (downstream contract), calls external API
```

**Contracts per layer:**
| Layer | Type | Purpose |
|-------|------|---------|
| Controller | `*Dto` | What the caller sends/receives |
| Manager/Service boundary | `*Entity` | Internal representation |
| Adapter | `*Proxy` | What the downstream API expects |

DTO→Entity translation happens in Service. Entity→Proxy translation happens in Adapter.

## Domains

### auth (`/auth`)
Proxies to the Rust auth API (`AUTH_API_URL`, default `localhost:4183`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/register` | Create user — forwards username, email, password + any extra fields |
| POST | `/auth/login` | Returns JWT from downstream auth service |

RegisterRequestDto captures extra JSON fields via `@JsonAnySetter` into a `Map<String, Object> extras` — these get forwarded as-is.

### progress (`/progress`)
Cross-device watch progress for shows-app. Proxies to the same Rust auth API
(`AUTH_API_URL`) — that service owns the Mongo collection and verifies the JWT,
so no persistence or token decoding happens here.

Every route requires `Authorization: Bearer <jwt>`; the header is forwarded
untouched. A missing header returns 401 via `GlobalExceptionHandler`.

| Method | Path | Description |
|--------|------|-------------|
| POST | `/progress` | Upsert `{show, path, position, duration, finished?}` |
| GET | `/progress` | Latest episode per show |
| GET | `/progress?show=X` | All episodes watched in show X, newest first |

`show` is the watch key (`"One Piece"`, or `"GOT/Season 03"` for seasonal
shows); `path` is the episode filename.

### notification (`/notification`)
Proxies to Mail-Service (`MAIL_API_URL`, default `localhost:7070`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/notification/send` | Dispatches a notification via mail service |

## Environment

```
PORT=4181
AUTH_API_URL=http://localhost:4183
MAIL_API_URL=http://localhost:7070
```

Copy `.env.example` → `.env` and fill in values.

## Build & Run

```bash
# Build
mvn clean package -q

# Run
java -jar target/mecca-api-project-1.0.0.jar
```

## Adding a new domain

1. Create package: `src/main/java/com/cursedshrine/apinexus/<domain>/`
2. Add sub-packages: `controller`, `dto`, `service`, `entity`, `manager`, `adapter`, `proxy`
3. Add a named `WebClient` bean in `WebClientConfig.java` with the downstream base URL
4. Add the base URL property to `application.properties` + `.env.example`

## Error Handling

`GlobalExceptionHandler` catches WebClientResponseException (downstream errors) and generic exceptions, returning structured JSON with `success: false` and the status code.
