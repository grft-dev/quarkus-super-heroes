import {GraftConfig, HeroesGraftFacade} from "@graft/maven-groupid_io-quarkus-sample_artifactid_rest-heroes_ri5cld";

let hostConfigured = false;

function resolveHeroesGraftHost() {
  const configured = window?.APP_CONFIG?.HEROES_GRAFT_HOST;
  if (configured) {
    return configured;
  }

  if (window?.APP_CONFIG?.CALCULATE_API_BASE_URL) {
    const wsProtocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    const host = window.location.host.replace("ui-super-heroes", "rest-heroes");
    return `${wsProtocol}//${host}/ws`;
  }

  return "ws://localhost:8090/ws";
}

function ensureGraftHost() {
  if (hostConfigured) {
    return;
  }
  GraftConfig.host = resolveHeroesGraftHost();
  hostConfigured = true;
}

function mapHeroDto(dto) {
  if (dto == null) {
    return undefined;
  }
  const name = dto.name ?? dto.getName?.();
  const levelRaw = dto.level ?? dto.getLevel?.();
  const level = typeof levelRaw === "number" ? levelRaw : Number(levelRaw);
  const picture = dto.picture ?? dto.getPicture?.();
  const powers = dto.powers ?? dto.getPowers?.();
  return {name, level, picture, powers};
}

/**
 * Loads a random hero via {@link HeroesGraftFacade#getRandomHero} (Graft WebSocket).
 * @returns {Promise<{name: string, level: number, picture: string, powers: string}|undefined>}
 */
export async function getRandomHeroFromGraft() {
  if (window?.APP_CONFIG?.HEROES_GRAFT_ENABLED === false) {
    return undefined;
  }

  ensureGraftHost();
  const facade = new HeroesGraftFacade();
  const dto = await Promise.resolve(facade.getRandomHero());
  return mapHeroDto(dto);
}
