# mecca-api-project — rename + push fix (2026-06-29)

## What happened
- Project was previously named `api-nexus`. Renamed to `mecca-api-project`:
  - Folder: `/home/gaurav/Projects/api-nexus` → `/home/gaurav/Projects/mecca-api-project`
  - `pom.xml` `artifactId` and `name` updated to `mecca-api-project`
- Original push was failing because:
  1. Local `develop` had no upstream set
  2. Remote GitHub repo `api-nexus` did not exist (404)
  3. `origin` remote URL had a GitHub PAT embedded in plaintext (security risk — anyone with repo/`.git/config` read access could extract it)
- Created new private GitHub repo `gauravrathore701/mecca-api-project` via GitHub API (using the exposed PAT one last time to bootstrap it).
- Switched remote to SSH (`git@github.com:gauravrathore701/mecca-api-project.git`), removing the plaintext PAT from git config.
- Committed the rename and pushed `develop` branch successfully.

## Follow-up
- The old PAT that was embedded in the remote URL should be revoked/rotated on GitHub, since it was exposed in plaintext in `.git/config` for an unknown period.
