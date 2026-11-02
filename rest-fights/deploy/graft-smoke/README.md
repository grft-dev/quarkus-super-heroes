# Graftcode smoke (rest-fights)

See [`rest-heroes/deploy/graft-smoke/README.md`](../../rest-heroes/deploy/graft-smoke/README.md) for the workflow.

| Endpoint | URL |
|----------|-----|
| **Vision** | **http://localhost:8092/GV** |
| **WebSocket** | `ws://localhost:8092/ws` |

Try `FightsGraftFacade.hello()` and `getRandomLocation()` after Mongo/Kafka dependencies are up (full stack compose) or minimal smoke DB in `docker-compose.yml`.
