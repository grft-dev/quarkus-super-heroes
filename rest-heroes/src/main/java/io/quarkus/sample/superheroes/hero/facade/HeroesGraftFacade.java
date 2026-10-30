package io.quarkus.sample.superheroes.hero.facade;

import java.lang.reflect.Method;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.sample.superheroes.hero.model.HeroDto;
import io.quarkus.sample.superheroes.hero.model.HeroPatchDto;

/**
 * Intentional Graftcode callable surface for hero operations (sync POJOs, no HTTP or reactive types).
 * Public types follow the portable baseline: primitives, {@code String}, plain models, and homogeneous arrays —
 * no {@code java.util.Optional} or {@code java.util.List} on this surface (Vision/GMA flags them unsupported).
 */
@ApplicationScoped
public class HeroesGraftFacade {

  private static final String EXECUTOR = "io.quarkus.sample.superheroes.hero.facade.HeroesGraftExecutor";

  /** Ping hello (same text as former {@code GET /api/heroes/hello}). */
  public String hello() {
    return "Hello Hero Resource";
  }

  /** Returns a random hero, or {@code null} if the database has no heroes. */
  public HeroDto getRandomHero() {
    return (HeroDto) invoke("getRandomHero");
  }

  /**
   * Returns all heroes, optionally filtered by name (case-insensitive substring match).
   *
   * @param nameFilter optional filter; {@code null} returns all heroes
   */
  public HeroDto[] getAllHeroes(String nameFilter) {
    return (HeroDto[]) invoke("getAllHeroes", nameFilter);
  }

  /** Returns a hero by id, or {@code null} if not found. */
  public HeroDto getHero(int id) {
    return (HeroDto) invoke("getHero", id);
  }

  /** Persists a new hero and returns its generated id. */
  public int createHero(HeroDto hero) {
    return (Integer) invoke("createHero", hero);
  }

  /**
   * Fully replaces an existing hero.
   *
   * @return {@code true} if the hero existed and was updated, {@code false} if not found
   */
  public boolean replaceHero(int id, HeroDto hero) {
    return (Boolean) invoke("replaceHero", id, hero);
  }

  /**
   * Partially updates a hero.
   *
   * @return the updated hero, or {@code null} if not found
   */
  public HeroDto partialUpdateHero(int id, HeroPatchDto patch) {
    return (HeroDto) invoke("partialUpdateHero", id, patch);
  }

  /** Deletes a hero by id (no-op if id does not exist). */
  public void deleteHero(int id) {
    invoke("deleteHero", id);
  }

  /** Deletes all heroes. */
  public void deleteAllHeroes() {
    invoke("deleteAllHeroes");
  }

  /** Replaces the entire hero table with the given heroes. */
  public void replaceAllHeroes(HeroDto[] heroes) {
    invoke("replaceAllHeroes", (Object) heroes);
  }

  private static Object invoke(String method, Object... args) {
    try {
      Class<?> executor = Class.forName(EXECUTOR);
      Method target = executor.getDeclaredMethod(method, parameterTypes(method, args));
      return target.invoke(null, args);
    } catch (ReflectiveOperationException e) {
      var cause = e.getCause() != null ? e.getCause() : e;
      if (cause instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      throw new IllegalStateException(cause);
    }
  }

  private static Class<?>[] parameterTypes(String method, Object[] args) throws NoSuchMethodException {
    return switch (method) {
      case "getAllHeroes" -> new Class<?>[] {String.class};
      case "getHero", "deleteHero" -> new Class<?>[] {int.class};
      case "createHero" -> new Class<?>[] {HeroDto.class};
      case "replaceHero" -> new Class<?>[] {int.class, HeroDto.class};
      case "partialUpdateHero" -> new Class<?>[] {int.class, HeroPatchDto.class};
      case "replaceAllHeroes" -> new Class<?>[] {HeroDto[].class};
      case "getRandomHero", "deleteAllHeroes" -> new Class<?>[0];
      default -> throw new NoSuchMethodException(method);
    };
  }
}
