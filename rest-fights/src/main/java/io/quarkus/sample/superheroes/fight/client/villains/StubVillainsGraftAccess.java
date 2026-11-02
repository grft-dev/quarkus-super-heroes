package io.quarkus.sample.superheroes.fight.client.villains;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import io.quarkus.sample.superheroes.fight.client.Villain;

@Alternative
@Priority(1)
@ApplicationScoped
@io.quarkus.arc.lookup.LookupIfProperty(name = "fight.villains.graft.access", stringValue = "stub")
public class StubVillainsGraftAccess implements VillainsGraftAccess {

  public enum RandomVillainBehavior {
    RETURN_CONFIGURED,
    RETURN_NULL,
    FAIL
  }

  private static volatile String helloResponse = "Hello Villain Resource";
  private static volatile Villain randomVillain;
  private static volatile RandomVillainBehavior randomVillainBehavior = RandomVillainBehavior.RETURN_CONFIGURED;
  private static volatile RuntimeException randomVillainFailure = new RuntimeException("graft villains failure");
  private static volatile long randomVillainDelayMs;
  private static volatile long helloDelayMs;
  private static volatile boolean helloFails;

  public static void reset() {
    helloResponse = "Hello Villain Resource";
    randomVillain = null;
    randomVillainBehavior = RandomVillainBehavior.RETURN_CONFIGURED;
    randomVillainFailure = new RuntimeException("graft villains failure");
    randomVillainDelayMs = 0;
    helloDelayMs = 0;
    helloFails = false;
  }

  public static void configureHello(String response) {
    helloResponse = response;
    helloDelayMs = 0;
  }

  public static void configureRandomVillain(Villain villain) {
    randomVillain = villain;
    randomVillainBehavior = RandomVillainBehavior.RETURN_CONFIGURED;
    randomVillainDelayMs = 0;
  }

  public static void configureRandomVillainNotFound() {
    randomVillainBehavior = RandomVillainBehavior.RETURN_NULL;
  }

  public static void configureRandomVillainFailure(RuntimeException failure) {
    randomVillainFailure = failure;
    randomVillainBehavior = RandomVillainBehavior.FAIL;
  }

  @Override
  public String hello() {
    if (helloFails) {
      throw new RuntimeException("villains graft hello failure");
    }
    sleepIfNeeded(helloDelayMs);
    return helloResponse;
  }

  @Override
  public Villain getRandomVillain() {
    sleepIfNeeded(randomVillainDelayMs);
    return switch (randomVillainBehavior) {
      case RETURN_NULL -> null;
      case FAIL -> throw randomVillainFailure;
      case RETURN_CONFIGURED -> randomVillain;
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
