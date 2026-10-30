package io.quarkus.sample.superheroes.fight.client.heroes;

import io.quarkus.sample.superheroes.fight.client.Hero;

/**
 * Sync access to {@code HeroesGraftFacade} ({@code hello}, {@code getRandomHero}) for use from {@link io.quarkus.sample.superheroes.fight.client.HeroClient}.
 */
public interface HeroesGraftAccess {

  String hello();

  /**
   * @return a random hero, or {@code null} when the heroes service has none (former HTTP 404)
   */
  Hero getRandomHero();
}
