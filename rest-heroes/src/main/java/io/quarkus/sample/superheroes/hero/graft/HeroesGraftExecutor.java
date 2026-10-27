package io.quarkus.sample.superheroes.hero.graft;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import io.quarkus.arc.Arc;
import io.quarkus.sample.superheroes.hero.model.HeroDto;
import io.quarkus.sample.superheroes.hero.model.HeroPatchDto;
import io.quarkus.sample.superheroes.hero.service.HeroService;
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle;

import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.ContextInternal;
import io.vertx.mutiny.core.Vertx;

/** Internal reactive bridge; loaded at runtime via reflection from {@link HeroesGraftFacade}. */
public final class HeroesGraftExecutor {

  private HeroesGraftExecutor() {}

  public static HeroDto getRandomHero() {
    return toNullable(await(() -> heroService().findRandomHeroApi()));
  }

  public static HeroDto[] getAllHeroes(String nameFilter) {
    List<HeroDto> list = await(() -> heroService().listHeroes(nameFilter));
    return list.toArray(HeroDto[]::new);
  }

  public static HeroDto getHero(int id) {
    return toNullable(await(() -> heroService().findHeroApiById(toEntityId(id))));
  }

  public static int createHero(HeroDto hero) {
    return await(() -> heroService().createHero(hero)).getId();
  }

  public static boolean replaceHero(int id, HeroDto hero) {
    return await(() -> heroService().replaceHero(toEntityId(id), hero));
  }

  public static HeroDto partialUpdateHero(int id, HeroPatchDto patch) {
    return toNullable(await(() -> heroService().partialUpdateHero(toEntityId(id), patch)));
  }

  public static void deleteHero(int id) {
    await(() -> heroService().deleteHero(toEntityId(id)));
  }

  public static void deleteAllHeroes() {
    await(() -> heroService().deleteAllHeroes());
  }

  public static void replaceAllHeroes(HeroDto[] heroes) {
    await(() -> heroService().replaceAllHeroesFromDtos(Arrays.asList(heroes)));
  }

  private static HeroDto toNullable(Optional<HeroDto> optional) {
    return optional.orElse(null);
  }

  private static Long toEntityId(int id) {
    return id == 0 ? null : (long) id;
  }

  private static HeroService heroService() {
    return Arc.container().instance(HeroService.class).get();
  }

  private static <T> T await(Supplier<Uni<T>> uniSupplier) {
    var vertx = Arc.container().instance(Vertx.class).get();
    var result = new CompletableFuture<T>();
    var duplicated = ((ContextInternal) vertx.getDelegate().getOrCreateContext()).duplicate();
    duplicated.runOnContext(v -> {
      VertxContextSafetyToggle.setCurrentContextSafe(true);
      uniSupplier.get().subscribe().with(result::complete, result::completeExceptionally);
    });

    try {
      return result.get(2, TimeUnit.MINUTES);
    } catch (Exception e) {
      var cause = e.getCause() != null ? e.getCause() : e;
      if (cause instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      throw new IllegalStateException(cause);
    }
  }
}
