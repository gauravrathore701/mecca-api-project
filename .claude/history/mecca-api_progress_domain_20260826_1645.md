# mecca-api-project — progress domain

**Date:** 2026-08-26 16:45 IST

## Why
shows-app needs cross-device watch progress. Gaurav's call: the DB stays in
the Rust auth service, mecca-api only proxies to it — so this project stays
a pure connector, no persistence layer added.

## New package
`com/cursedshrine/apinexus/progress/` — same Controller → Service → Manager
→ Adapter pipeline as `auth`, reusing the existing `authWebClient` bean
(base URL `AUTH_API_URL`, default `localhost:4183`).

- `dto/ProgressRequestDto.java`, `dto/ProgressResponseDto.java`
- `entity/ProgressEntity.java`
- `proxy/ProgressProxy.java` — `@JsonInclude(NON_NULL)` so an absent
  `finished` lets the downstream compute it from the ratio
- `manager/ProgressManager.java`, `service/ProgressService.java`
- `adapter/ProgressAdapter.java` — forwards the caller's `Authorization`
  header untouched; no JWT decoding happens here
- `controller/ProgressController.java`

## Modified
- `exception/GlobalExceptionHandler.java` — added a
  `MissingRequestHeaderException` handler returning **401**. Without it a
  call with no `Authorization` header fell through to the generic handler
  and returned 500.

## Endpoints
Context path is `/api` (set by `SERVER_SERVLET_CONTEXT_PATH` in the
systemd unit, not in `application.properties`).

- `POST /api/progress` — body `{show, path, position, duration, finished?}`
- `GET  /api/progress` — latest episode per show
- `GET  /api/progress?show=X` — all episodes for show X

## Verified
Rebuilt, restarted the service, and exercised all three routes against a
throwaway account: save, read-all, read-one with a show name containing
both a space and a slash (`GOT/Season 03`), plus 401 on missing header and
401 passthrough on a garbage token.

## Note for later
`CLAUDE.md` in this repo is stale in two places — it names the jar
`api-nexus-1.0.0.jar` (actually `mecca-api-project-1.0.0.jar`) and omits
the `/api` context path. Updated in this changeset.
