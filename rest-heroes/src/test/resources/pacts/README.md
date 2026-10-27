# Pact (legacy REST heroes)

Provider verification for **REST** `rest-fights` → `rest-heroes` is **removed** — fights call heroes via **Graft** (`HeroesGraftFacade`), not `/api/heroes`.

Other services (e.g. `rest-villains`, `rest-narration`) still use Pact where applicable.
