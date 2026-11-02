import {GraftConfig, VillainsGraftFacade} from "@graft/maven-groupid_io-quarkus-sample_artifactid_rest-villains_ri5cld";

let hostConfigured = false;

function resolveVillainsGraftHost() {
  const configured = window?.APP_CONFIG?.VILLAINS_GRAFT_HOST;
  if (configured) {
    return configured;
  }

  if (window?.APP_CONFIG?.CALCULATE_API_BASE_URL) {
    const wsProtocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    const host = window.location.host.replace("ui-super-heroes", "rest-villains");
    return `${wsProtocol}//${host}/ws`;
  }

  return "ws://localhost:8091/ws";
}

function ensureGraftHost() {
  if (hostConfigured) {
    return;
  }
  GraftConfig.host = resolveVillainsGraftHost();
  hostConfigured = true;
}

function mapVillainDto(dto) {
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

export async function getRandomVillainFromGraft() {
  if (window?.APP_CONFIG?.VILLAINS_GRAFT_ENABLED === false) {
    return undefined;
  }

  ensureGraftHost();
  const facade = new VillainsGraftFacade();
  const dto = await Promise.resolve(facade.getRandomVillain());
  return mapVillainDto(dto);
}
