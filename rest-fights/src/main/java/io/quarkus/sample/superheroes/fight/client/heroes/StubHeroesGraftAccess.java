package io.quarkus.sample.superheroes.fight.client.heroes;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import io.quarkus.sample.superheroes.fight.client.Hero;

/**
 * In-process stand-in for the heroes Graft facade used in tests (no HTTP to rest-heroes).
 * Integration and unit tests configure {@link #reset()} / setters before invoking the application.
 */
@Alternative
@Priority(1)
@ApplicationScoped
@io.quarkus.arc.lookup.LookupIfProperty(name = "fight.heroes.graft.access", stringValue = "stub")
public class StubHeroesGraftAccess implements HeroesGraftAccess {

  public enum RandomHeroBehavior {
    RETURN_CONFIGURED,
    RETURN_NULL,
    FAIL
  }

  private static volatile String helloResponse = "Hello Hero Resource";
  private static volatile Hero randomHero;
  private static volatile RandomHeroBehavior randomHeroBehavior = RandomHeroBehavior.RETURN_CONFIGURED;
  private static volatile RuntimeException randomHeroFailure = new RuntimeException("graft heroes failure");
  private static volatile long randomHeroDelayMs;
  private static volatile long helloDelayMs;
  private static volatile boolean helloFails;

  public static void reset() {
    helloResponse = "Hello Hero Resource";
    randomHero = null;
    randomHeroBehavior = RandomHeroBehavior.RETURN_CONFIGURED;
    randomHeroFailure = new RuntimeException("graft heroes failure");
    randomHeroDelayMs = 0;
    helloDelayMs = 0;
    helloFails = false;
  }

  public static void configureHello(String response) {
    helloResponse = response;
    helloDelayMs = 0;
  }

  public static void configureHelloDelayed(String response, long delayMs) {
    helloResponse = response;
    helloDelayMs = delayMs;
    helloFails = false;
  }

  public static void configureHelloFailure() {
    helloFails = true;
  }

  public static void configureRandomHero(Hero hero) {
    randomHero = hero;
    randomHeroBehavior = RandomHeroBehavior.RETURN_CONFIGURED;
    randomHeroDelayMs = 0;
  }

  public static void configureRandomHeroDelayed(Hero hero, long delayMs) {
    randomHero = hero;
    randomHeroBehavior = RandomHeroBehavior.RETURN_CONFIGURED;
    randomHeroDelayMs = delayMs;
  }

  public static void configureRandomHeroNotFound() {
    randomHeroBehavior = RandomHeroBehavior.RETURN_NULL;
  }

  public static void configureRandomHeroFailure(RuntimeException failure) {
    randomHeroFailure = failure;
    randomHeroBehavior = RandomHeroBehavior.FAIL;
  }

  @Override
  public String hello() {
    if (helloFails) {
      throw new RuntimeException("heroes graft hello failure");
    }
    sleepIfNeeded(helloDelayMs);
    return helloResponse;
  }

  @Override
  public Hero getRandomHero() {
    sleepIfNeeded(randomHeroDelayMs);
    return switch (randomHeroBehavior) {
      case RETURN_NULL -> null;
      case FAIL -> throw randomHeroFailure;
      case RETURN_CONFIGURED -> randomHero;
    };
  }

  private static void sleepIfNeeded(long delayMs) {
    if (delayMs <= 0) {
      return;
    }
    try {
      Thread.sleep(delayMs);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
