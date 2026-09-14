# Mecca API — notification route + error-message leak fixed (2026-09-13 17:15)

## Bug 1: /notification/send never worked
- Mecca sent {to, from, subject, body} to Mail-Service POST /save/subscriber, which
  only accepts {name, email} → 422 on every call. No callers existed.
- Mail-Service has no send capability, so the route now matches what it can do:
  `POST /api/notification/subscribe` {name, email} → 201 {success, "subscriber saved"}.
  400 on blank name / malformed email. DTO/Entity/Proxy/Service/Manager/Adapter reshaped;
  adapter uses toBodilessEntity() (Mail-Service returns 201 with empty body).
- Old `/notification/send` removed (now 404).
- Underlying second cause (Mail-Service repo/unit): its systemd unit had
  `Environment=MONGODB_URI=mongodb://localhost:27017` (no credentials). dotenv does not
  override existing env vars, so the service ignored its .env and every insert failed
  "requires authentication" since Mongo --auth (2026-07-15). Fixed in Mail-Service unit.

## Bug 2: error responses leaked internal URLs
- Was: "downstream error: 400 Bad Request from POST http://localhost:4183/users/login";
  generic handler also returned raw exception text.
- GlobalExceptionHandler rewritten: downstream status + downstream's own short JSON
  error/message (≤200 chars) else reason phrase; 502 for unreachable downstream;
  400 malformed JSON; 404 unknown route; 405 wrong method; 500 "internal error".
  Full detail logged server-side only (@Slf4j).

## Deploy
- `mvn -q clean package -DskipTests`, `systemctl restart mecca-api-project` (x2).
  Not deploy.sh (it runs git pull).

## Backups
- `.claude/backups/bugfix-20260913-1650/` (notification + exception packages, old jar, CLAUDE.md)

## Verified (public https://api.cursedshrine.com/api)
- login {} → 400 "username required"; bad creds → 401 "invalid credentials";
  bad JSON → 400; progress no auth → 401; bad token → 401 "invalid or expired token";
  subscribe invalid → 400; old /send → 404; GET subscribe → 405.
- subscribe valid → 201, row present in notification_service.subscribers with
  client_id api-nexus; test row deleted afterwards (total back to 8).
- No response contains localhost / internal ports.
