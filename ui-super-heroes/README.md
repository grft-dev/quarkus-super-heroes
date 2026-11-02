# Superheroes Battle UI

> **Full documentation: [https://quarkus.io/quarkus-super-heroes/ui-super-heroes](https://quarkus.io/quarkus-super-heroes/ui-super-heroes)**

## Introduction

This is the main user interface for the application. The application is a React application served via [Quinoa](https://quarkus.io/extensions/io.quarkiverse.quinoa/quarkus-quinoa).

The main UI allows you to pick up one random Hero and Villain by clicking on "New Fighters", then pick a random location for the fight to take place by clicking on "New Location". Then it's just a matter of clicking on "Fight!" to get them to fight. The table at the bottom shows the list of the previous fights.

![ui-super-heroes](images/ui-super-heroes.png)

## Running the Application

From the `quarkus-super-heroes/ui-super-heroes` directory, simply run `quarkus dev` to start both the Quarkus server and the React hot reloading server at http://localhost:3000.

The application uses [Microcks](https://microcks.io) in dev mode by default to provide mock responses from downstream services, so no other services need to be running.

### Graftcode (browser) — heroes, villains, battles

| Facade | WebSocket (full stack compose) |
|--------|--------------------------------|
| `HeroesGraftFacade` | `ws://localhost:8090/ws` |
| `VillainsGraftFacade` | `ws://localhost:8091/ws` |
| `FightsGraftFacade` | `ws://localhost:8092/ws` |

Random fighters use **heroes + villains** Graft clients (`heroes-graft-service.js`, `villains-graft-service.js`). Fights list, start fight, location, narration, and image use **`fights-graft-service.js`**.

Publish each service with [`deploy/graft-smoke/`](../rest-heroes/deploy/graft-smoke/README.md) (heroes / villains / fights modules). Install the three npm packages from Vision logs; until then `graft-stub*` folders keep CI green.

Env: `HEROES_GRAFT_HOST`, `VILLAINS_GRAFT_HOST`, `FIGHTS_GRAFT_HOST` via `/env.js`.

![main-ui](images/main-ui.png)
