package io.quarkus.sample.superheroes.fight.client.villains;

import io.quarkus.sample.superheroes.fight.client.Villain;

/** Test helpers replacing former WireMock stubs for {@code /api/villains/*}. */
public final class VillainGraftStubSupport {

  private VillainGraftStubSupport() {
  }

  public static void reset() {
    StubVillainsGraftAccess.reset();
  }

  public static void stubRandomVillain(Villain villain) {
    StubVillainsGraftAccess.configureRandomVillain(villain);
  }

  public static void stubRandomVillainNotFound() {
    StubVillainsGraftAccess.configureRandomVillainNotFound();
  }

  public static void stubHello(String response) {
    StubVillainsGraftAccess.configureHello(response);
  }

  public static void stubHelloDefault() {
    stubHello("Hello Villain Resource");
  }
}
