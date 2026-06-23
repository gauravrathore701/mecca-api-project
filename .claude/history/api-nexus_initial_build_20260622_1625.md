# api-nexus — Initial Build

**Date:** 2026-06-22 16:25

## What was done

Created the `api-nexus` project from scratch — a Java 21 / Spring Boot 3.3.5 middleware connector.

## Prerequisites installed

- `maven` (3.9.9 via apt)
- `openjdk-21-jdk` (javac was missing, only JRE was present)

## Architecture

Strict pipeline: Controller → Service → Manager → Adapter

Contracts:
- Controller level: DTO (`*Dto`)
- Service↔Manager boundary: Entity (`*Entity`)
- Adapter level: Proxy (`*Proxy`)

DTO→Entity translation in Service. Entity→Proxy translation in Adapter.

## Domains built

1. **auth** — proxies to Rust auth API at `localhost:4179`
   - POST `/auth/register` — supports extra fields via `@JsonAnySetter`
   - POST `/auth/login`

2. **notification** — proxies to Mail-Service at `localhost:7070`
   - POST `/notification/send`

## Port

4181 (4180 was taken by discord-claude-bot)

## Test result

Build: `mvn clean package -q` → success, JAR produced at `target/api-nexus-1.0.0.jar`

Live test: `POST /auth/login` returned `{"status":404,"success":false,"message":"downstream error: 404 Not Found from POST http://localhost:4179/users/login"}` — correct behavior (Rust auth service not running, GlobalExceptionHandler properly formatted the error).

## Files created

```
pom.xml
.env.example
.gitignore
CLAUDE.md
src/main/resources/application.properties
src/main/java/com/cursedshrine/apinexus/
  ApiNexusApplication.java
  config/WebClientConfig.java
  exception/GlobalExceptionHandler.java
  auth/{controller,dto,service,entity,manager,adapter,proxy}/
  notification/{controller,dto,service,entity,manager,adapter,proxy}/
```
