package io.quarkus.sample.superheroes.fight.facade;

import io.quarkus.arc.Arc;
import io.quarkus.sample.superheroes.fight.model.FightDto;
import io.quarkus.sample.superheroes.fight.model.FightImageDto;
import io.quarkus.sample.superheroes.fight.model.FightLocationDto;
import io.quarkus.sample.superheroes.fight.model.FightRequestDto;
import io.quarkus.sample.superheroes.fight.model.FightToNarrateDto;
import io.quarkus.sample.superheroes.fight.model.ImageGenerationRequestDto;
import io.quarkus.sample.superheroes.fight.service.FightService;

/** Internal reactive bridge; loaded at runtime via reflection from {@link FightsGraftFacade}. */
public final class FightsGraftExecutor {

  private FightsGraftExecutor() {}

  public static FightDto[] getFights(int page, int size) {
    return FightsGraftModelMapping.toFightDtos(
      fightService().findFights(page, size).await().indefinitely()
    );
  }

  public static FightDto performFight(FightRequestDto fightRequest) {
    return FightsGraftModelMapping.toFightDto(
      fightService().performFight(FightsGraftModelMapping.toDomain(fightRequest)).await().indefinitely()
    );
  }

  public static FightLocationDto getRandomLocation() {
    return FightsGraftModelMapping.toLocationDto(
      fightService().findRandomLocation().await().indefinitely()
    );
  }

  public static String narrateFight(FightToNarrateDto fight) {
    return fightService().narrateFight(FightsGraftModelMapping.toDomain(fight)).await().indefinitely();
  }

  public static FightImageDto generateImageFromNarration(ImageGenerationRequestDto request) {
    return FightsGraftModelMapping.toFightImageDto(
      fightService().generateImageFromNarration(FightsGraftModelMapping.toDomain(request)).await().indefinitely()
    );
  }

  private static FightService fightService() {
    return Arc.container().instance(FightService.class).get();
  }
}
