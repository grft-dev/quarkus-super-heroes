package io.quarkus.sample.superheroes.fight.facade;

import java.lang.reflect.Method;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.sample.superheroes.fight.model.FightDto;
import io.quarkus.sample.superheroes.fight.model.FightImageDto;
import io.quarkus.sample.superheroes.fight.model.FightLocationDto;
import io.quarkus.sample.superheroes.fight.model.FightRequestDto;
import io.quarkus.sample.superheroes.fight.model.FightToNarrateDto;
import io.quarkus.sample.superheroes.fight.model.ImageGenerationRequestDto;

/**
 * Intentional Graftcode callable surface for fight operations (sync POJOs, no HTTP or reactive types).
 * Public types follow the portable baseline: primitives, {@code String}, plain models, and homogeneous arrays —
 * no {@code java.util.Optional} or {@code java.util.List} on this surface (Vision/GMA flags them unsupported).
 */
@ApplicationScoped
public class FightsGraftFacade {

  public static final String HELLO_MESSAGE = "Hello Fight Resource";

  private static final String EXECUTOR = "io.quarkus.sample.superheroes.fight.facade.FightsGraftExecutor";

  /** Ping hello (same text as {@code GET /api/fights/hello}). */
  public String hello() {
    return HELLO_MESSAGE;
  }

  /** Returns a page of persisted fights, newest first. */
  public FightDto[] getFights(int page, int size) {
    return (FightDto[]) invoke("getFights", page, size);
  }

  /** Performs a fight, persists it, and publishes the Avro event. */
  public FightDto performFight(FightRequestDto fightRequest) {
    return (FightDto) invoke("performFight", fightRequest);
  }

  /** Returns a random fight location (with service fallbacks when downstream is unavailable). */
  public FightLocationDto getRandomLocation() {
    return (FightLocationDto) invoke("getRandomLocation");
  }

  /** Narrates a completed fight via the narration service. */
  public String narrateFight(FightToNarrateDto fight) {
    return (String) invoke("narrateFight", fight);
  }

  /** Generates an image from narration text via the narration service. */
  public FightImageDto generateImageFromNarration(ImageGenerationRequestDto request) {
    return (FightImageDto) invoke("generateImageFromNarration", request);
  }

  private static Object invoke(String method, Object... args) {
    try {
      Class<?> executor = Class.forName(EXECUTOR);
      Method target = executor.getDeclaredMethod(method, parameterTypes(method));
      return target.invoke(null, args);
    } catch (ReflectiveOperationException e) {
      var cause = e.getCause() != null ? e.getCause() : e;
      if (cause instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      throw new IllegalStateException(cause);
    }
  }

  private static Class<?>[] parameterTypes(String method) throws NoSuchMethodException {
    return switch (method) {
      case "getFights" -> new Class<?>[] {int.class, int.class};
      case "performFight" -> new Class<?>[] {FightRequestDto.class};
      case "getRandomLocation" -> new Class<?>[0];
      case "narrateFight" -> new Class<?>[] {FightToNarrateDto.class};
      case "generateImageFromNarration" -> new Class<?>[] {ImageGenerationRequestDto.class};
      default -> throw new NoSuchMethodException(method);
    };
  }
}
