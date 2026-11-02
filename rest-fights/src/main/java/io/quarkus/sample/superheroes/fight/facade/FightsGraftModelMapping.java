package io.quarkus.sample.superheroes.fight.facade;

import java.util.List;

import io.quarkus.sample.superheroes.fight.Fight;
import io.quarkus.sample.superheroes.fight.FightImage;
import io.quarkus.sample.superheroes.fight.FightLocation;
import io.quarkus.sample.superheroes.fight.FightRequest;
import io.quarkus.sample.superheroes.fight.ImageGenerationRequest;
import io.quarkus.sample.superheroes.fight.client.FightToNarrate;
import io.quarkus.sample.superheroes.fight.client.Hero;
import io.quarkus.sample.superheroes.fight.client.Villain;
import io.quarkus.sample.superheroes.fight.model.FightDto;
import io.quarkus.sample.superheroes.fight.model.FightImageDto;
import io.quarkus.sample.superheroes.fight.model.FightLocationDto;
import io.quarkus.sample.superheroes.fight.model.FightRequestDto;
import io.quarkus.sample.superheroes.fight.model.FightToNarrateDto;
import io.quarkus.sample.superheroes.fight.model.FightToNarrateLocationDto;
import io.quarkus.sample.superheroes.fight.model.FighterDto;
import io.quarkus.sample.superheroes.fight.model.ImageGenerationRequestDto;

/** Maps between domain types and Graft-facing DTOs. */
final class FightsGraftModelMapping {

  private FightsGraftModelMapping() {}

  static FightDto[] toFightDtos(List<Fight> fights) {
    return fights.stream().map(FightsGraftModelMapping::toFightDto).toArray(FightDto[]::new);
  }

  static FightDto toFightDto(Fight fight) {
    var dto = new FightDto()
      .setId(fight.id != null ? fight.id.toString() : null)
      .setFightDate(fight.fightDate != null ? fight.fightDate.toString() : null)
      .setWinnerName(fight.winnerName)
      .setWinnerLevel(fight.winnerLevel)
      .setWinnerPowers(fight.winnerPowers)
      .setWinnerPicture(fight.winnerPicture)
      .setLoserName(fight.loserName)
      .setLoserLevel(fight.loserLevel)
      .setLoserPowers(fight.loserPowers)
      .setLoserPicture(fight.loserPicture)
      .setWinnerTeam(fight.winnerTeam)
      .setLoserTeam(fight.loserTeam);
    if (fight.location != null) {
      dto.setLocation(toLocationDto(fight.location));
    }
    return dto;
  }

  static FightRequest toDomain(FightRequestDto request) {
    return new FightRequest(
      toHero(request.getHero()),
      toVillain(request.getVillain()),
      toDomainLocation(request.getLocation())
    );
  }

  static FightLocationDto toLocationDto(FightLocation location) {
    if (location == null) {
      return new FightLocationDto();
    }
    return new FightLocationDto()
      .setName(location.name())
      .setDescription(location.description())
      .setPicture(location.picture());
  }

  static FightLocation toDomainLocation(FightLocationDto location) {
    if (location == null) {
      return new FightLocation();
    }
    return new FightLocation(location.getName(), location.getDescription(), location.getPicture());
  }

  static FightToNarrate toDomain(FightToNarrateDto dto) {
    var location = dto.getLocation();
    return new FightToNarrate(
      dto.getWinnerTeam(),
      dto.getWinnerName(),
      dto.getWinnerPowers(),
      dto.getWinnerLevel(),
      dto.getLoserTeam(),
      dto.getLoserName(),
      dto.getLoserPowers(),
      dto.getLoserLevel(),
      new FightToNarrate.FightToNarrateLocation(
        location != null ? location.getName() : null,
        location != null ? location.getDescription() : null
      )
    );
  }

  static ImageGenerationRequest toDomain(ImageGenerationRequestDto dto) {
    return new ImageGenerationRequest(dto.getNarration(), dto.getWinnerPictureUrl(), dto.getLoserPictureUrl());
  }

  static FightImageDto toFightImageDto(FightImage image) {
    return new FightImageDto().setImageUrl(image.imageUrl());
  }

  private static Hero toHero(FighterDto fighter) {
    return new Hero(fighter.getName(), fighter.getLevel(), fighter.getPicture(), fighter.getPowers());
  }

  private static Villain toVillain(FighterDto fighter) {
    return new Villain(fighter.getName(), fighter.getLevel(), fighter.getPicture(), fighter.getPowers());
  }
}
