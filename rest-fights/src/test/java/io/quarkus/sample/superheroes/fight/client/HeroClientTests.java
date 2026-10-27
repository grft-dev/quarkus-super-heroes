package io.quarkus.sample.superheroes.fight.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.stream.IntStream;

import jakarta.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;

import io.quarkus.sample.superheroes.fight.client.heroes.HeroGraftStubSupport;

import io.smallrye.faulttolerance.api.CircuitBreakerMaintenance;
import io.smallrye.faulttolerance.api.CircuitBreakerState;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

/**
 * Tests for {@link HeroClient} against the in-process Graft stub (not HTTP).
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HeroClientTests {
  private static final String DEFAULT_HERO_NAME = "Super Baguette";
  private static final String DEFAULT_HERO_PICTURE = "super_baguette.png";
  private static final String DEFAULT_HERO_POWERS = "eats baguette really quickly";
  private static final int DEFAULT_HERO_LEVEL = 42;
  private static final String DEFAULT_HELLO_RESPONSE = "Hello Hero Resource";

  private static final Hero DEFAULT_HERO = new Hero(
    DEFAULT_HERO_NAME,
    DEFAULT_HERO_LEVEL,
    DEFAULT_HERO_PICTURE,
    DEFAULT_HERO_POWERS
  );

  @Inject
  HeroClient heroClient;

  @Inject
  CircuitBreakerMaintenance circuitBreakerMaintenance;

  @BeforeEach
  void beforeEach() {
    HeroGraftStubSupport.reset();
    this.circuitBreakerMaintenance.resetAll();
  }

  @AfterEach
  void afterEach() {
    this.circuitBreakerMaintenance.resetAll();
  }

  @Test
  @Order(1)
  void findsRandom() {
    HeroGraftStubSupport.stubRandomHero(DEFAULT_HERO);

    IntStream.range(0, 5)
      .forEach(_ -> {
        var hero = this.heroClient.findRandomHero()
          .subscribe().withSubscriber(UniAssertSubscriber.create())
          .awaitSubscription(Duration.ofSeconds(5))
          .awaitItem(Duration.ofSeconds(5))
          .getItem();

        assertThat(hero)
          .isNotNull()
          .isEqualTo(DEFAULT_HERO);
      });
  }

  @Test
  @Order(2)
  void recoversFrom404() {
    HeroGraftStubSupport.stubRandomHeroNotFound();

    IntStream.range(0, 5)
      .forEach(_ -> this.heroClient.findRandomHero()
        .subscribe().withSubscriber(UniAssertSubscriber.create())
        .awaitSubscription(Duration.ofSeconds(5))
        .awaitItem(Duration.ofSeconds(5))
        .assertItem(null)
      );
  }

  @Test
  @Order(4)
  void doesntRecoverFrom500() {
    HeroGraftStubSupport.stubRandomHeroServerError();

    assertThat(this.circuitBreakerMaintenance.currentState("findRandomHero"))
      .isEqualTo(CircuitBreakerState.CLOSED);

    IntStream.rangeClosed(1, 2)
      .forEach(_ ->
        this.heroClient.findRandomHero()
          .subscribe().withSubscriber(UniAssertSubscriber.create())
          .awaitSubscription(Duration.ofSeconds(5))
          .awaitFailure(Duration.ofSeconds(5))
          .assertFailedWith(RuntimeException.class)
      );

    var ex = this.heroClient.findRandomHero()
      .subscribe().withSubscriber(UniAssertSubscriber.create())
      .awaitSubscription(Duration.ofSeconds(5))
      .awaitFailure(Duration.ofSeconds(5))
      .getFailure();

    assertThat(ex)
      .isNotNull()
      .isExactlyInstanceOf(org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException.class)
      .hasMessageContainingAll("%s#findRandomHero".formatted(HeroClient.class.getName()), "circuit breaker is open");

    assertThat(this.circuitBreakerMaintenance.currentState("findRandomHero"))
      .isEqualTo(CircuitBreakerState.OPEN);
  }

  @Test
  @Order(3)
  void helloHeroes() {
    HeroGraftStubSupport.stubHello(DEFAULT_HELLO_RESPONSE);

    this.heroClient.helloHeroes()
      .subscribe().withSubscriber(UniAssertSubscriber.create())
      .awaitSubscription(Duration.ofSeconds(5))
      .awaitItem(Duration.ofSeconds(5))
      .assertItem(DEFAULT_HELLO_RESPONSE);
  }
}
