# Graftcode smoke (rest-villains)

Manual end-to-end check: **GG publish → Vision → invoke**.

The same `Dockerfile` is used by full-stack Docker Compose (`deploy/docker-compose/java25.yml`) so `rest-fights` can reach `ws://rest-villains:80/ws`. Run `mvn package` before `docker compose build`.

## Run stack

```powershell
cd rest-villains
$env:JAVA_HOME = "$env:LOCALAPPDATA\jdks\temurin-25"
.\mvnw.cmd -DskipTests package

cd deploy\graft-smoke
docker compose up -d --build
```

| Endpoint | URL |
|----------|-----|
| **Vision + Try it out** | **http://localhost:8091/GV** |
| **Graft WebSocket** | `ws://localhost:8091/ws` |

Smoke `hello()` and `getRandomVillain()` on `VillainsGraftFacade`.

## Stop

```powershell
docker compose down
```
