# Superheroes Villain Microservice

> **Full documentation: [https://quarkus.io/quarkus-super-heroes/rest-villains](https://quarkus.io/quarkus-super-heroes/rest-villains)**

## Introduction

Villain data lives in PostgreSQL. **Callable integration** for other services is through **Graftcode** — `VillainsGraftFacade` (`io.quarkus.sample.superheroes.villain.facade`), not a public REST CRUD API.

HTTP on port `8084` is for **operations only** (health, metrics, Dev UI in dev mode).

![rest-villains](images/rest-villains.png)

## Running the Application

From `quarkus-super-heroes/rest-villains`, run `./mvnw quarkus:dev`. Health: http://localhost:8084/q/health

Outside dev mode you need PostgreSQL (see `application.properties` for defaults).
