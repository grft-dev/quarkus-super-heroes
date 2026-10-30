# Superheroes Battle UI

> **Full documentation: [https://quarkus.io/quarkus-super-heroes/ui-super-heroes](https://quarkus.io/quarkus-super-heroes/ui-super-heroes)**

## Introduction

This is the main user interface for the application. The application is a React application served via [Quinoa](https://quarkus.io/extensions/io.quarkiverse.quinoa/quarkus-quinoa).

The main UI allows you to pick up one random Hero and Villain by clicking on "New Fighters", then pick a random location for the fight to take place by clicking on "New Location". Then it's just a matter of clicking on "Fight!" to get them to fight. The table at the bottom shows the list of the previous fights.

![ui-super-heroes](images/ui-super-heroes.png)

## Running the Application

From the `quarkus-super-heroes/ui-super-heroes` directory, simply run `quarkus dev` to start both the Quarkus server and the React hot reloading server at http://localhost:3000.

The application uses [Microcks](https://microcks.io) in dev mode by default to provide mock responses from downstream services, so no other services need to be running.

### Heroes via Graftcode (browser)

Random **heroes** are loaded from **`HeroesGraftFacade`** over WebSocket (`heroes-graft-service.js`), not from heroes REST. Villains still come from `rest-fights` (`/api/fights/randomfighters`).

1. Publish heroes Graft (see [`rest-heroes/deploy/graft-smoke/README.md`](../rest-heroes/deploy/graft-smoke/README.md)).
2. Install the generated npm package in `src/main/webui` (command from Vision / `docker logs` on GG).
3. Set `heroes.graft.host` (env `HEROES_GRAFT_HOST`) — default `ws://localhost:8090/ws` for full-stack Docker Compose.

Until the real package is installed, `graft-stub` is used so CI builds; replace it with the `@graft/maven-groupid_io-quarkus-sample_artifactid_rest-heroes_ri5cld` package from your registry.

![main-ui](images/main-ui.png)
