# Smoke results (2026-10-27)

| Step | Result |
|------|--------|
| `mvn -DskipTests package` | OK |
| GG `Type enabled: HeroesGraftFacade` | OK |
| UGM upload / IDL in logs | OK |
| Vision **http://localhost:8080/GV** | OK — open on **8080** so Try it out uses `ws://localhost:8080/ws` |
| Invoke `hello()` via GV Run | OK — `Hello Hero Resource` |
| Facade types (no red in GV) | OK after `int` / `HeroDto[]` / nullable `HeroDto` (no Optional/List/Long on surface) |
| Container `heroes-gg` | Up |
| npm install `@graft/...` | **ETARGET** — virtual feed on `d.grft.dev` does not serve the package (infra/GPGE; not fixed locally) |

**npm on Windows:** use `npm.cmd` if execution policy blocks `npm.ps1`.

**Next for full caller path:** team/GPGE — npm package visibility after UGM upload; optional Maven caller under `caller-maven/`.
