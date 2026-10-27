# Superheroes Hero Microservice

> **Full documentation: [https://quarkus.io/quarkus-super-heroes/rest-heroes](https://quarkus.io/quarkus-super-heroes/rest-heroes)**

## Introduction

Heroes data lives in PostgreSQL. **Callable integration** for other services is through **Graftcode** — `HeroesGraftFacade` (`io.quarkus.sample.superheroes.hero.graft`), not a public REST CRUD API.

HTTP on port `8083` is for **operations only** (health, metrics, Dev UI in dev mode).

![rest-heroes](images/rest-heroes.png)

## Running the Application

From `quarkus-super-heroes/rest-heroes`, run `./mvnw quarkus:dev`. Health: http://localhost:8083/q/health

Outside dev mode you need PostgreSQL (see `application.yml` for defaults).

## Graft receiver (local smoke)

To run Quarkus with **Grafting Gateway** (WebSocket `ws://…/ws`, Vision `/GV`), build the app and use [`deploy/graft-smoke/`](deploy/graft-smoke/README.md).

## Running Locally (full stack)

See the [Running Locally](https://quarkus.io/quarkus-super-heroes/running-locally) guide. `rest-fights` reaches heroes via `FIGHT_HEROES_GRAFT_HOST` (default in compose/k8s: `ws://rest-heroes:80/ws`).
