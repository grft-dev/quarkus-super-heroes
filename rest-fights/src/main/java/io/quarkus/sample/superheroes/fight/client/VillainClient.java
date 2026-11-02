package io.quarkus.sample.superheroes.fight.client;

import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

import io.quarkus.logging.Log;
import io.quarkus.sample.superheroes.fight.client.villains.VillainsGraftAccess;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.faulttolerance.api.CircuitBreakerName;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

@ApplicationScoped
public class VillainClient {
  private final VillainsGraftAccess villainsGraftAccess;

  public VillainClient(VillainsGraftAccess villainsGraftAccess) {
    this.villainsGraftAccess = villainsGraftAccess;
  }

  @CircuitBreaker(requestVolumeThreshold = 8, failureRatio = 0.5, delay = 2, delayUnit = ChronoUnit.SECONDS)
  @CircuitBreakerName("findRandomVillain")
  @Retry(maxRetries = 3, delay = 200, delayUnit = ChronoUnit.MILLIS)
  @WithSpan(kind = SpanKind.CLIENT, value = "VillainClient.findRandomVillain")
  public Uni<Villain> findRandomVillain() {
    Log.debug("Calling villains graft facade to find random villain");
    return Uni.createFrom().item(villainsGraftAccess::getRandomVillain)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }

  @WithSpan(kind = SpanKind.CLIENT, value = "VillainClient.helloVillains")
  public Uni<String> helloVillains() {
    Log.debug("Calling villains graft facade hello");
    return Uni.createFrom().item(villainsGraftAccess::hello)
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }
}
