package io.quarkus.sample.superheroes.fight.client.heroes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.logging.Log;
import io.quarkus.sample.superheroes.fight.client.Hero;
import io.quarkus.sample.superheroes.fight.config.FightConfig;

/**
 * Calls the published {@code HeroesGraftFacade} through the generated Graft Maven package (Hypertube WebSocket).
 * Uses reflection so the build does not require the virtual registry artifact on every CI run.
 */
@ApplicationScoped
public class GraftRemoteHeroesAccess implements HeroesGraftAccess {

  private static final String FACADE_CLASS = "io.quarkus.sample.superheroes.hero.graft.HeroesGraftFacade";

  private static final String[] GRAFT_CONFIG_CLASS_CANDIDATES = {
    "graft.maven.io.quarkus.sample.rest_heroes_ri5cld.GraftConfig",
    "graft.maven.io.quarkus.sample.artifactid_rest_heroes_ri5cld.GraftConfig",
    "graft.maven.io.quarkus.sample.artifactid_rest-heroes_ri5cld.GraftConfig"
  };

  private final FightConfig.Heroes.Graft graftConfig;

  private volatile Object facade;

  public GraftRemoteHeroesAccess(FightConfig fightConfig) {
    this.graftConfig = fightConfig.heroes().graft();
  }

  @Override
  public String hello() {
    return (String) invokeFacade("hello");
  }

  @Override
  public Hero getRandomHero() {
    var dto = invokeFacade("getRandomHero");
    if (dto == null) {
      return null;
    }
    try {
      return mapHeroDto(dto);
    }
    catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Failed to map heroes graft DTO", e);
    }
  }

  private Object invokeFacade(String methodName) {
    try {
      var target = facadeInstance();
      Method method = target.getClass().getMethod(methodName);
      return method.invoke(target);
    }
    catch (ReflectiveOperationException e) {
      var cause = e.getCause() != null ? e.getCause() : e;
      if (cause instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      throw new IllegalStateException("Heroes graft call failed: " + methodName, cause);
    }
  }

  private Object facadeInstance() throws ReflectiveOperationException {
    if (facade != null) {
      return facade;
    }
    synchronized (this) {
      if (facade != null) {
        return facade;
      }
      configureGraftHost();
      Class<?> facadeClass = Class.forName(FACADE_CLASS);
      Constructor<?> constructor = facadeClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      facade = constructor.newInstance();
      Log.debugf("Heroes Graft facade ready (host=%s)", graftConfig.host());
      return facade;
    }
  }

  private void configureGraftHost() throws ReflectiveOperationException {
    Class<?> graftConfigClass = resolveGraftConfigClass();
    setStaticField(graftConfigClass, "host", graftConfig.host());
    if (graftConfig.stateless()) {
      try {
        setStaticField(graftConfigClass, "stateless", true);
      }
      catch (NoSuchFieldException ignored) {
        // optional on older generated runtimes
      }
    }
  }

  private Class<?> resolveGraftConfigClass() throws ClassNotFoundException {
    var override = graftConfig.configClass().orElse("");
    if (!override.isBlank()) {
      return Class.forName(override);
    }
    for (var candidate : GRAFT_CONFIG_CLASS_CANDIDATES) {
      try {
        return Class.forName(candidate);
      }
      catch (ClassNotFoundException ignored) {
        // try next naming convention from GPGE
      }
    }
    throw new ClassNotFoundException(
      "GraftConfig not on classpath — install the heroes Graft Maven package and set fight.heroes.graft.config-class if needed");
  }

  private static void setStaticField(Class<?> type, String fieldName, Object value) throws ReflectiveOperationException {
    Field field = type.getField(fieldName);
    field.set(null, value);
  }

  private static Hero mapHeroDto(Object dto) throws ReflectiveOperationException {
    Class<?> dtoClass = dto.getClass();
    var name = (String) dtoClass.getMethod("getName").invoke(dto);
    var levelValue = dtoClass.getMethod("getLevel").invoke(dto);
    int level = levelValue instanceof Integer integer ? integer : ((Number) levelValue).intValue();
    var picture = (String) dtoClass.getMethod("getPicture").invoke(dto);
    var powers = (String) dtoClass.getMethod("getPowers").invoke(dto);
    return new Hero(name, level, picture, powers);
  }
}
