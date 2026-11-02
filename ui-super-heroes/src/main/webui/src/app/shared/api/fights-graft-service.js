import {GraftConfig, FightsGraftFacade} from "@graft/maven-groupid_io-quarkus-sample_artifactid_rest-fights_ri5cld";

let hostConfigured = false;

function resolveFightsGraftHost() {
  const configured = window?.APP_CONFIG?.FIGHTS_GRAFT_HOST;
  if (configured) {
    return configured;
  }

  if (window?.APP_CONFIG?.CALCULATE_API_BASE_URL) {
    const wsProtocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    const host = window.location.host.replace("ui-super-heroes", "rest-fights");
    return `${wsProtocol}//${host}/ws`;
  }

  return "ws://localhost:8092/ws";
}

function ensureGraftHost() {
  if (hostConfigured) {
    return;
  }
  GraftConfig.host = resolveFightsGraftHost();
  hostConfigured = true;
}

function mapFightDto(dto) {
  if (dto == null) {
    return undefined;
  }
  const id = dto.id ?? dto.getId?.();
  const fightDate = dto.fightDate ?? dto.getFightDate?.();
  return {
    id,
    fightDate,
    winnerName: dto.winnerName ?? dto.getWinnerName?.(),
    winnerLevel: dto.winnerLevel ?? dto.getWinnerLevel?.(),
    winnerPowers: dto.winnerPowers ?? dto.getWinnerPowers?.(),
    winnerPicture: dto.winnerPicture ?? dto.getWinnerPicture?.(),
    loserName: dto.loserName ?? dto.getLoserName?.(),
    loserLevel: dto.loserLevel ?? dto.getLoserLevel?.(),
    loserPowers: dto.loserPowers ?? dto.getLoserPowers?.(),
    loserPicture: dto.loserPicture ?? dto.getLoserPicture?.(),
    winnerTeam: dto.winnerTeam ?? dto.getWinnerTeam?.(),
    loserTeam: dto.loserTeam ?? dto.getLoserTeam?.(),
    location: mapLocationDto(dto.location ?? dto.getLocation?.()),
  };
}

function mapLocationDto(dto) {
  if (dto == null) {
    return undefined;
  }
  return {
    name: dto.name ?? dto.getName?.(),
    description: dto.description ?? dto.getDescription?.(),
    picture: dto.picture ?? dto.getPicture?.(),
  };
}

function mapFightLocationDto(dto) {
  return mapLocationDto(dto);
}

function mapFightImageDto(dto) {
  if (dto == null) {
    return undefined;
  }
  return {imageUrl: dto.imageUrl ?? dto.getImageUrl?.()};
}

function toFighterDto(fighter) {
  return {
    name: fighter.name,
    level: fighter.level,
    picture: fighter.picture,
    powers: fighter.powers,
  };
}

function toFightRequestDto(body) {
  return {
    hero: toFighterDto(body.hero),
    villain: toFighterDto(body.villain),
    location: body.location ? {
      name: body.location.name,
      description: body.location.description,
      picture: body.location.picture,
    } : undefined,
  };
}

function toFightToNarrateDto(body) {
  return {
    winnerTeam: body.winnerTeam,
    winnerName: body.winnerName,
    winnerPowers: body.winnerPowers,
    winnerLevel: body.winnerLevel,
    loserTeam: body.loserTeam,
    loserName: body.loserName,
    loserPowers: body.loserPowers,
    loserLevel: body.loserLevel,
    location: body.location ? {
      name: body.location.name,
      description: body.location.description,
    } : undefined,
  };
}

export async function getFightsFromGraft(options = {}) {
  if (window?.APP_CONFIG?.FIGHTS_GRAFT_ENABLED === false) {
    return undefined;
  }
  ensureGraftHost();
  const page = options.page ?? 0;
  const size = options.size ?? 20;
  const facade = new FightsGraftFacade();
  const fights = await Promise.resolve(facade.getFights(page, size));
  if (!fights) {
    return [];
  }
  return fights.map(mapFightDto);
}

export async function startFightFromGraft(body) {
  ensureGraftHost();
  const facade = new FightsGraftFacade();
  const dto = await Promise.resolve(facade.performFight(toFightRequestDto(body)));
  return mapFightDto(dto);
}

export async function getRandomLocationFromGraft() {
  if (window?.APP_CONFIG?.FIGHTS_GRAFT_ENABLED === false) {
    return undefined;
  }
  ensureGraftHost();
  const facade = new FightsGraftFacade();
  const dto = await Promise.resolve(facade.getRandomLocation());
  return mapFightLocationDto(dto);
}

export async function narrateFightFromGraft(body) {
  ensureGraftHost();
  const facade = new FightsGraftFacade();
  return Promise.resolve(facade.narrateFight(toFightToNarrateDto(body)));
}

export async function generateImageFromGraft(narration, winnerPictureUrl, loserPictureUrl) {
  ensureGraftHost();
  const facade = new FightsGraftFacade();
  const dto = await Promise.resolve(facade.generateImageFromNarration({
    narration,
    winnerPictureUrl,
    loserPictureUrl,
  }));
  return mapFightImageDto(dto);
}
