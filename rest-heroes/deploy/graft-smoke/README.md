# Graftcode smoke (rest-heroes)

Manual end-to-end check: **GG publish → Vision → invoke** (npm caller optional).

The same `Dockerfile` is used by **full-stack Docker Compose** (`rest-heroes/src/main/docker-compose/*.yml` and root `deploy/docker-compose/*`) so `rest-fights` can reach `ws://rest-heroes:80/ws`. Run `mvn package` before `docker compose build`.

## Prerequisites

- Java **25** (`$env:JAVA_HOME` → Temurin 25)
- Docker Desktop
- `mvn package` in [`rest-heroes`](../../) (produces `target/quarkus-app/`)

## Run stack

```powershell
cd rest-heroes
$env:JAVA_HOME = "$env:LOCALAPPDATA\jdks\temurin-25"
.\mvnw.cmd -DskipTests package

cd deploy\graft-smoke
docker compose up -d --build
```

| Endpoint | URL |
|----------|-----|
| **Vision + Try it out** | **http://localhost:8080/GV** |
| **Graft WebSocket** | `ws://localhost:8080/ws` |
| GG liveness | http://localhost:8080/status |
| Vision (alt listener in container) | http://localhost:8081/GV — use **:8080** for Run; GV picks WS from the browser port (`8081/GV` → `ws://localhost:8081/ws` fails unless you map extra ports) |
| Postgres | `heroes-db-graft-smoke` on compose network |

Install commands and GUIDs: `docker logs heroes-gg` (GUID changes after each restart).

## Invoke (recommended smoke)

1. Open **http://localhost:8080/GV**
2. `HeroesGraftFacade` → **`hello()`** (instance method) → **Run**
3. Expected: `Hello Hero Resource`

Optional: `getAllHeroes` with `null` filter, `getHero` with a valid id from DB.

## Surface / GMA notes (Quarkus)

- **`graft-publish.jar`** — minimal JAR (facade + DTOs) for `gg … --graftOnly`
- **Runtime** — Quarkus in background + `gg` on `app/rest-heroes-1.0.jar` with `--types HeroesGraftFacade`
- **Executor** — reflection bridge so GMA sees sync POJO API only
- **Types** — portable baseline: `int`, `String`, `boolean`, `HeroDto[]`, nullable `HeroDto`; no `Long`, `java.util.Optional`, `java.util.List` on the facade

## Caller smoke (Node, optional)

GUID from logs (first `npm install` block after publish):

```powershell
cd deploy\graft-smoke\caller
npm.cmd install --registry "https://d.grft.dev/<GUID>__free" "@graft/maven-groupid_io-quarkus-sample_artifactid_rest-heroes_ri5cld@1.0"
```

On Windows use **`npm.cmd`** if PowerShell blocks `npm.ps1` (execution policy).

Set `GraftConfig.host = "ws://localhost:8080/ws"` (or `GRAFTCODE_CONFIG_…` env with the same host).

If **`ETARGET` / notarget** on `d.grft.dev`: UGM upload can succeed while the npm feed has no package — use Vision **Try it out** for invoke; escalate to Graftcode/GPGE infra if Node caller is required.

## Stop

```powershell
docker compose down
```
