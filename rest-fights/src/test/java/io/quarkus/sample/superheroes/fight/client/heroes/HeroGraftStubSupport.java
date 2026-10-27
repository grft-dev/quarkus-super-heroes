package io.quarkus.sample.superheroes.fight.client.heroes;

import io.quarkus.sample.superheroes.fight.client.Hero;

/**
 * Test helpers replacing former WireMock stubs for {@code /api/heroes/*}.
 */
public final class HeroGraftStubSupport {

  private HeroGraftStubSupport() {
  }

  public static void reset() {
    StubHeroesGraftAccess.reset();
  }

  public static void stubRandomHero(Hero hero) {
    StubHeroesGraftAccess.configureRandomHero(hero);
  }

  public static void stubRandomHeroDelayed(Hero hero, long delayMs) {
    StubHeroesGraftAccess.configureRandomHeroDelayed(hero, delayMs);
  }

  public static void stubRandomHeroNotFound() {
    StubHeroesGraftAccess.configureRandomHeroNotFound();
  }

  public static void stubRandomHeroServerError() {
    StubHeroesGraftAccess.configureRandomHeroFailure(new RuntimeException("heroes graft remote error"));
  }

  public static void stubHello(String response) {
    StubHeroesGraftAccess.configureHello(response);
  }

  public static void stubHelloDelayed(String response, long delayMs) {
    StubHeroesGraftAccess.configureHelloDelayed(response, delayMs);
  }

  public static void stubHelloDefault() {
    StubHeroesGraftAccess.configureHello("Hello Hero Resource");
  }

  public static void stubHelloFailure() {
    StubHeroesGraftAccess.configureHelloFailure();
  }
}
