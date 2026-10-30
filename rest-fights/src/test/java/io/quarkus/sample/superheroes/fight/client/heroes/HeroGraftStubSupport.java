package io.quarkus.sample.superheroes.fight.client.heroes;

import static io.restassured.http.ContentType.JSON;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import io.quarkus.sample.superheroes.fight.client.Hero;
import io.quarkus.sample.superheroes.fight.client.heroes.HeroesGraftStubControlResource.HelloCommand;
import io.quarkus.sample.superheroes.fight.client.heroes.HeroesGraftStubControlResource.RandomHeroBehavior;
import io.quarkus.sample.superheroes.fight.client.heroes.HeroesGraftStubControlResource.RandomHeroCommand;

import io.restassured.RestAssured;

/**
 * Test helpers replacing former WireMock stubs for {@code /api/heroes/*}.
 * <p>
 *   For {@code @QuarkusIntegrationTest}, call {@link #setRemoteControl(boolean)} so configuration
 *   is sent to the running application via {@link HeroesGraftStubControlResource}.
 * </p>
 */
public final class HeroGraftStubSupport {

  private static final String BASE_PATH = "/api/fights/__test/heroes-graft-stub";

  private static volatile boolean remoteControl;

  private HeroGraftStubSupport() {
  }

  /** Enable when tests run against a packaged app ({@code @QuarkusIntegrationTest}). */
  public static void setRemoteControl(boolean enabled) {
    remoteControl = enabled;
  }

  public static void reset() {
    if (remoteControl) {
      RestAssured.given().post(BASE_PATH + "/reset").then().statusCode(org.hamcrest.Matchers.anyOf(
        org.hamcrest.Matchers.is(200),
        org.hamcrest.Matchers.is(204)
      ));
    }
    else {
      StubHeroesGraftAccess.reset();
    }
  }

  public static void stubRandomHero(Hero hero) {
    if (remoteControl) {
      postRandomHero(new RandomHeroCommand(
        RandomHeroBehavior.RETURN_CONFIGURED,
        hero.name(),
        hero.level(),
        hero.picture(),
        hero.powers(),
        0,
        null
      ));
    }
    else {
      StubHeroesGraftAccess.configureRandomHero(hero);
    }
  }

  public static void stubRandomHeroDelayed(Hero hero, long delayMs) {
    if (remoteControl) {
      postRandomHero(new RandomHeroCommand(
        RandomHeroBehavior.RETURN_CONFIGURED,
        hero.name(),
        hero.level(),
        hero.picture(),
        hero.powers(),
        delayMs,
        null
      ));
    }
    else {
      StubHeroesGraftAccess.configureRandomHeroDelayed(hero, delayMs);
    }
  }

  public static void stubRandomHeroNotFound() {
    if (remoteControl) {
      postRandomHero(new RandomHeroCommand(RandomHeroBehavior.RETURN_NULL, null, 0, null, null, 0, null));
    }
    else {
      StubHeroesGraftAccess.configureRandomHeroNotFound();
    }
  }

  public static void stubRandomHeroServerError() {
    if (remoteControl) {
      postRandomHero(new RandomHeroCommand(
        RandomHeroBehavior.FAIL,
        null,
        0,
        null,
        null,
        0,
        "heroes graft remote error"
      ));
    }
    else {
      StubHeroesGraftAccess.configureRandomHeroFailure(
        new WebApplicationException("heroes graft remote error", Response.Status.INTERNAL_SERVER_ERROR)
      );
    }
  }

  public static void stubHello(String response) {
    if (remoteControl) {
      postHello(new HelloCommand(response, 0, false));
    }
    else {
      StubHeroesGraftAccess.configureHello(response);
    }
  }

  public static void stubHelloDelayed(String response, long delayMs) {
    if (remoteControl) {
      postHello(new HelloCommand(response, delayMs, false));
    }
    else {
      StubHeroesGraftAccess.configureHelloDelayed(response, delayMs);
    }
  }

  public static void stubHelloDefault() {
    stubHello("Hello Hero Resource");
  }

  public static void stubHelloFailure() {
    if (remoteControl) {
      postHello(new HelloCommand("", 0, true));
    }
    else {
      StubHeroesGraftAccess.configureHelloFailure();
    }
  }

  private static void postHello(HelloCommand command) {
    RestAssured.given()
      .contentType(JSON)
      .body(command)
      .post(BASE_PATH + "/hello")
      .then()
      .statusCode(anySuccess());
  }

  private static void postRandomHero(RandomHeroCommand command) {
    RestAssured.given()
      .contentType(JSON)
      .body(command)
      .post(BASE_PATH + "/random-hero")
      .then()
      .statusCode(anySuccess());
  }

  private static org.hamcrest.Matcher<Integer> anySuccess() {
    return org.hamcrest.Matchers.anyOf(org.hamcrest.Matchers.is(200), org.hamcrest.Matchers.is(204));
  }
}
