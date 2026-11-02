package io.quarkus.sample.superheroes.fight.client;

import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

import io.quarkus.logging.Log;
import io.quarkus.sample.superheroes.fight.client.heroes.HeroesGraftAccess;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.faulttolerance.api.CircuitBreakerName;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

/**
 * Bean used for interacting with the Hero service via {@link HeroesGraftAccess} ({@code HeroesGraftFacade} contract).
 */
@ApplicationScoped
public class HeroClient {
	private final HeroesGraftAccess heroesGraftAccess;

	public HeroClient(HeroesGraftAccess heroesGraftAccess) {
		this.heroesGraftAccess = heroesGraftAccess;
	}

	/**
	 * Finds a random {@link Hero}. The retry logic is applied to the result of the {@link CircuitBreaker}.
	 * @return A random {@link Hero}, or {@code null} when the heroes service has none
	 */
  @CircuitBreaker(requestVolumeThreshold = 8, failureRatio = 0.5, delay = 2, delayUnit = ChronoUnit.SECONDS)
	@CircuitBreakerName("findRandomHero")
  @Retry(maxRetries = 3, delay = 200, delayUnit = ChronoUnit.MILLIS)
  @WithSpan(kind = SpanKind.CLIENT, value = "HeroClient.findRandomHero")
	public Uni<Hero> findRandomHero() {
    Log.debug("Calling heroes graft facade to find random hero");
		return Uni.createFrom().item(heroesGraftAccess::getRandomHero)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}
  
	/**
	 * Calls hello on the Heroes graft facade.
	 * @return A "hello" from Heroes
	 */
  @WithSpan(kind = SpanKind.CLIENT, value = "HeroClient.helloHeroes")
	public Uni<String> helloHeroes() {
    Log.debug("Calling heroes graft facade hello");
		return Uni.createFrom().item(heroesGraftAccess::hello)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
	}
}
