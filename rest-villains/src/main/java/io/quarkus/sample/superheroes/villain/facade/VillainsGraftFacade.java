package io.quarkus.sample.superheroes.villain.facade;

import java.lang.reflect.Method;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.model.VillainPatchDto;

/**
 * Intentional Graftcode callable surface for villain operations (sync POJOs, no HTTP or reactive types).
 * Public types follow the portable baseline: primitives, {@code String}, plain models, and homogeneous arrays —
 * no {@code java.util.Optional} or {@code java.util.List} on this surface (Vision/GMA flags them unsupported).
 */
@ApplicationScoped
public class VillainsGraftFacade {

  private static final String EXECUTOR = "io.quarkus.sample.superheroes.villain.facade.VillainsGraftExecutor";

  /** Ping hello (same text as former {@code GET /api/villains/hello}). */
  public String hello() {
    return "Hello Villain Resource";
  }

  /** Returns a random villain, or {@code null} if the database has no villains. */
  public VillainDto getRandomVillain() {
    return (VillainDto) invoke("getRandomVillain");
  }

  /**
   * Returns all villains, optionally filtered by name (case-insensitive substring match).
   *
   * @param nameFilter optional filter; {@code null} returns all villains
   */
  public VillainDto[] getAllVillains(String nameFilter) {
    return (VillainDto[]) invoke("getAllVillains", nameFilter);
  }

  /** Returns a villain by id, or {@code null} if not found. */
  public VillainDto getVillain(int id) {
    return (VillainDto) invoke("getVillain", id);
  }

  /** Persists a new villain and returns its generated id. */
  public int createVillain(VillainDto villain) {
    return (Integer) invoke("createVillain", villain);
  }

  /**
   * Fully replaces an existing villain.
   *
   * @return {@code true} if the villain existed and was updated, {@code false} if not found
   */
  public boolean replaceVillain(int id, VillainDto villain) {
    return (Boolean) invoke("replaceVillain", id, villain);
  }

  /**
   * Partially updates a villain.
   *
   * @return the updated villain, or {@code null} if not found
   */
  public VillainDto partialUpdateVillain(int id, VillainPatchDto patch) {
    return (VillainDto) invoke("partialUpdateVillain", id, patch);
  }

  /** Deletes a villain by id (no-op if id does not exist). */
  public void deleteVillain(int id) {
    invoke("deleteVillain", id);
  }

  /** Deletes all villains. */
  public void deleteAllVillains() {
    invoke("deleteAllVillains");
  }

  /** Replaces the entire villain table with the given villains. */
  public void replaceAllVillains(VillainDto[] villains) {
    invoke("replaceAllVillains", (Object) villains);
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
      case "getAllVillains" -> new Class<?>[] {String.class};
      case "getVillain", "deleteVillain" -> new Class<?>[] {int.class};
      case "createVillain" -> new Class<?>[] {VillainDto.class};
      case "replaceVillain" -> new Class<?>[] {int.class, VillainDto.class};
      case "partialUpdateVillain" -> new Class<?>[] {int.class, VillainPatchDto.class};
      case "replaceAllVillains" -> new Class<?>[] {VillainDto[].class};
      case "getRandomVillain", "deleteAllVillains" -> new Class<?>[0];
      default -> throw new NoSuchMethodException(method);
    };
  }
}
