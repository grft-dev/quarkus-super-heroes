/**
 * CI/dev placeholder. Replace by installing the generated package, e.g.
 * npm install --registry "https://d.grft.dev/<GUID>__free" "@graft/maven-groupid_io-quarkus-sample_artifactid_rest-heroes_ri5cld@1.0"
 * (see rest-heroes/deploy/graft-smoke/README.md)
 */
export const GraftConfig = {
  host: "inmemory",
};

export class HeroesGraftFacade {
  getRandomHero() {
    throw new Error(
      "Heroes Graft npm package is not installed — use the npm install command from Graft Vision / gg logs.",
    );
  }
}
