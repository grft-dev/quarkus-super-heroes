---
title: Heroes microservice (Graftcode)
description: Hero data in PostgreSQL, exposed to other services via Graftcode HeroesGraftFacade.
layout: page
content-toc: true
---

## Introduction

The heroes service stores hero data in PostgreSQL using [Hibernate Reactive with Panache's repository pattern](https://quarkus.io/guides/hibernate-reactive-panache#solution-2-using-the-repository-pattern).

**Integration surface:** [`HeroesGraftFacade`](https://github.com/quarkusio/quarkus-super-heroes/blob/main/rest-heroes/src/main/java/io/quarkus/sample/superheroes/hero/graft/HeroesGraftFacade.java) (`io.quarkus.sample.superheroes.hero.graft`) with DTOs in `io.quarkus.sample.superheroes.hero.model`. Callers (e.g. `rest-fights`) use a Graft WebSocket host such as `ws://rest-heroes:80/ws`, not HTTP CRUD on `/api/heroes`.

HTTP on port **8083** remains for **Quarkus operations** — health (`/q/health`), metrics, and Dev UI in dev mode.

![rest-heroes]({site.image('rest-heroes.png')})

### Callable facade (Graft)

| Operation | Description |
|-----------|-------------|
| `hello()` | Ping string (same intent as the former REST hello endpoint) |
| `getAllHeroes()` | All heroes as `HeroDto[]` |
| `getHero(int id)` | Hero by id, or null |
| `getRandomHero()` | Random hero, or null if none |
| `createHero(HeroDto)` | Create hero |
| `replaceHero(int id, HeroDto)` | Full replace |
| `patchHero(int id, HeroPatchDto)` | Partial update |
| `deleteHero(int id)` | Delete by id |
| `deleteAllHeroes()` | Delete all |

Local **Gateway + publish smoke:** `rest-heroes/deploy/graft-smoke/README.md`.

## Contract testing

**REST Pact** between `rest-fights` and `rest-heroes` is **removed** — fights integrate via Graft stubs in tests. Villains and narration still use Pact where documented in those services.

## Running the Application

From `quarkus-super-heroes/rest-heroes`, run `./mvnw quarkus:dev`. Health: http://localhost:8083/q/health

**NOTE:** Running outside dev mode requires PostgreSQL. Defaults:

| Description | Environment Variable | Java Property | Value |
|---|---|---|---|
| Database URL | `QUARKUS_DATASOURCE_REACTIVE_URL` | `quarkus.datasource.reactive.url` | `postgresql://localhost:5432/heroes_database` |
| Database username | `QUARKUS_DATASOURCE_USERNAME` | `quarkus.datasource.username` | `superman` |
| Database password | `QUARKUS_DATASOURCE_PASSWORD` | `quarkus.datasource.password` | `superman` |

## Full stack (Docker Compose / Kubernetes)

In generated compose and k8s config, `rest-fights` sets `fight.heroes.graft.host` / `FIGHT_HEROES_GRAFT_HOST` to `ws://rest-heroes:80/ws`. The heroes container image used in compose is built with **Grafting Gateway** on port **80** (see `deploy/graft-smoke/Dockerfile`).
