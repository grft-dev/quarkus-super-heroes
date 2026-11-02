package io.quarkus.sample.superheroes.fight.client.villains;

import io.quarkus.sample.superheroes.fight.client.Villain;

/**
 * Sync access to {@code VillainsGraftFacade} ({@code hello}, {@code getRandomVillain}) for use from {@link io.quarkus.sample.superheroes.fight.client.VillainClient}.
 */
public interface VillainsGraftAccess {

  String hello();

  /**
   * @return a random villain, or {@code null} when the villains service has none (former HTTP 404)
   */
  Villain getRandomVillain();
}
