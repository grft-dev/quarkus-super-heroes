# Superheroes Fight Microservice

> **Full documentation: [https://quarkus.io/quarkus-super-heroes/rest-fights](https://quarkus.io/quarkus-super-heroes/rest-fights)**

## Introduction

This is the Fight microservice. It orchestrates fights between [Heroes](../rest-heroes) and [Villains](../rest-villains), in a location from the [Location Service](../grpc-locations), then calls the [Narration service](../rest-narration) for narration and image generation.

**Callable integration for the Battle UI** is through **Graftcode** — `FightsGraftFacade` (`io.quarkus.sample.superheroes.fight.facade`) with portable DTOs in `io.quarkus.sample.superheroes.fight.model`. Configure `FIGHTS_GRAFT_HOST` (compose default on the host: `ws://localhost:8092/ws`).

HTTP on port `8082` still exposes the **contract-first** REST API (generated from [`src/main/resources/openapi/openapi.yml`](src/main/resources/openapi/openapi.yml) via the [Quarkiverse OpenAPI Generator Server extension](https://docs.quarkiverse.io/quarkus-openapi-generator/dev/server.html)) for operations, tooling, and existing clients.

Each fight is persisted in MongoDB and published on the `fights` Kafka topic ([Apache Avro](https://avro.apache.org/docs/current) via [Apicurio Schema Registry](https://www.apicur.io/registry)).

Fight messages are also published on an Apache Kafka topic called `fights`. The [event-statistics service](../event-statistics) listens for these events. Messages are stored in [Apache Avro](https://avro.apache.org/docs/current) format and the fight schema is automatically registered in the [Apicurio Schema Registry](https://www.apicur.io/registry).

![rest-fights](images/rest-fights.png)

## Running the Application

The application runs on port `8082` (defined by `quarkus.http.port` in [`application.properties`](src/main/resources/application.properties)).

From the `quarkus-super-heroes/rest-fights` directory, simply run `./mvnw quarkus:dev` to run [Quarkus Dev Mode](https://quarkus.io/guides/maven-tooling#dev-mode). The application will be exposed at `http://localhost:8082` and the [Quarkus Dev UI](https://quarkus.io/guides/dev-ui) will be exposed at `http://localhost:8082/q/dev`. [Quarkus Dev Services](https://quarkus.io/guides/dev-services) will ensure the MongoDB instance, an Apache Kafka instance, and an Apicurio Schema Registry are all started and configured.

## Graft receiver (local smoke)

To run Quarkus with **Grafting Gateway** (WebSocket `ws://…/ws`, Vision `/GV`), build the app (`./mvnw package -DskipTests`) and use [`deploy/graft-smoke/`](deploy/graft-smoke/docker-compose.yml) (host port **8092** → container **80**).

## Running Locally (full stack)

See the [Running Locally](https://quarkus.io/quarkus-super-heroes/running-locally) guide. Compose maps Graft on **8092** for `FightsGraftFacade`; heroes and villains use `8090` / `8091`.
