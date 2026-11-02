# Run rest-fights from this branch against docker-compose downstream (UI stays on :8080).
# Prerequisites:
#   1. docker compose -f deploy/docker-compose/java25.yml up -d  (without rest-fights, or stop rest-fights-java25)
#   2. mvn -DskipTests -Pgraft-runtime package in rest-fights (GUID in rest-fights/pom.xml profile or from: docker logs rest-heroes-java25)
#   3. Mongo port from: docker ps --filter name=fights-db --format "{{.Ports}}"

$ErrorActionPreference = "Stop"
$env:JAVA_HOME = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { "$env:LOCALAPPDATA\jdks\temurin-25" }

$mongoPort = if ($env:QUARKUS_MONGODB_HOSTS) { $env:QUARKUS_MONGODB_HOSTS } else { "localhost:58978" }

$env:QUARKUS_HTTP_PORT = "8082"
$env:QUARKUS_MICROCKS_DEVSERVICES_ENABLED = "false"
$env:FIGHT_HEROES_GRAFT_HOST = "ws://localhost:8090/ws"
$env:QUARKUS_STORK_VILLAIN_SERVICE_SERVICE_DISCOVERY_ADDRESS_LIST = "localhost:8084"
$env:QUARKUS_STORK_NARRATION_SERVICE_SERVICE_DISCOVERY_ADDRESS_LIST = "localhost:8087"
$env:QUARKUS_GRPC_CLIENTS_LOCATIONS_HOST = "localhost"
$env:QUARKUS_GRPC_CLIENTS_LOCATIONS_PORT = "8089"
$env:QUARKUS_MONGODB_HOSTS = $mongoPort
$env:QUARKUS_MONGODB_CREDENTIALS_USERNAME = "superfight"
$env:QUARKUS_MONGODB_CREDENTIALS_PASSWORD = "superfight"
$env:KAFKA_BOOTSTRAP_SERVERS = "PLAINTEXT://localhost:9092"
$env:MP_MESSAGING_CONNECTOR_SMALLRYE_KAFKA_APICURIO_REGISTRY_URL = "http://localhost:8086/apis/registry/v2"
$env:QUARKUS_OTEL_SDK_DISABLED = "true"
$env:QUARKUS_LIQUIBASE_MONGODB_MIGRATE_AT_START = "false"

Set-Location "$PSScriptRoot\..\rest-fights"
Write-Host "Starting rest-fights on http://localhost:8082 (heroes Graft ws://localhost:8090/ws)..."
& "$env:JAVA_HOME\bin\java.exe" -jar target\quarkus-app\quarkus-run.jar
