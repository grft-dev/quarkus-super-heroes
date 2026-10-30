package io.quarkus.sample.superheroes.fight.client.heroes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkus.sample.superheroes.fight.client.Hero;

/**
 * Test-only control plane for {@link StubHeroesGraftAccess} when {@code fight.heroes.graft.access=stub}.
 * Used by {@code @QuarkusIntegrationTest} because the application runs in a separate JVM.
 */
@ApplicationScoped
@Path("/api/fights/__test/heroes-graft-stub")
@io.quarkus.arc.lookup.LookupIfProperty(name = "fight.heroes.graft.access", stringValue = "stub")
public class HeroesGraftStubControlResource {

  @POST
  @Path("reset")
  public void reset() {
    StubHeroesGraftAccess.reset();
  }

  @POST
  @Path("hello")
  @Consumes(MediaType.APPLICATION_JSON)
  public void configureHello(HelloCommand command) {
    if (command.fail()) {
      StubHeroesGraftAccess.configureHelloFailure();
      return;
    }
    if (command.delayMs() > 0) {
      StubHeroesGraftAccess.configureHelloDelayed(command.response(), command.delayMs());
    }
    else {
      StubHeroesGraftAccess.configureHello(command.response());
    }
  }

  @POST
  @Path("random-hero")
  @Consumes(MediaType.APPLICATION_JSON)
  public void configureRandomHero(RandomHeroCommand command) {
    switch (command.behavior()) {
      case RETURN_NULL -> StubHeroesGraftAccess.configureRandomHeroNotFound();
      case FAIL -> StubHeroesGraftAccess.configureRandomHeroFailure(
        new WebApplicationException(
          command.errorMessage() != null ? command.errorMessage() : "heroes graft remote error",
          Response.Status.INTERNAL_SERVER_ERROR
        )
      );
      case RETURN_CONFIGURED -> {
        if (command.delayMs() > 0) {
          StubHeroesGraftAccess.configureRandomHeroDelayed(toHero(command), command.delayMs());
        }
        else {
          StubHeroesGraftAccess.configureRandomHero(toHero(command));
        }
      }
    }
  }

  private static Hero toHero(RandomHeroCommand command) {
    return new Hero(command.name(), command.level(), command.picture(), command.powers());
  }

  public record HelloCommand(String response, long delayMs, boolean fail) {
  }

  public enum RandomHeroBehavior {
    RETURN_CONFIGURED,
    RETURN_NULL,
    FAIL
  }

  public record RandomHeroCommand(
    RandomHeroBehavior behavior,
    String name,
    int level,
    String picture,
    String powers,
    long delayMs,
    String errorMessage
  ) {
  }
}
