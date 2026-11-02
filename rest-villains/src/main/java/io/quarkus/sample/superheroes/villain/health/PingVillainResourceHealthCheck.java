package io.quarkus.sample.superheroes.villain.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import io.quarkus.sample.superheroes.villain.facade.VillainsGraftFacade;

/**
 * {@link HealthCheck} to ping the villain Graftcode facade.
 */
@Liveness
public class PingVillainResourceHealthCheck implements HealthCheck {
  private final VillainsGraftFacade villainsGraftFacade;

  public PingVillainResourceHealthCheck(VillainsGraftFacade villainsGraftFacade) {
    this.villainsGraftFacade = villainsGraftFacade;
  }

  @Override
  public HealthCheckResponse call() {
    var response = this.villainsGraftFacade.hello();

    return HealthCheckResponse.named("Ping Villain Graft Facade")
      .withData("Response", response)
      .up()
      .build();
  }
}
