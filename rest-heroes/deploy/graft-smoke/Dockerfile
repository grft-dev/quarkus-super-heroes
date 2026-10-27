# Graftcode smoke receiver: pre-built Quarkus fast-jar (run mvn package on host) + Gateway.
FROM eclipse-temurin:25-jdk-jammy AS runtime

ARG GG_VERSION=latest
RUN apt-get update && apt-get install -y --no-install-recommends wget ca-certificates && rm -rf /var/lib/apt/lists/* \
  && if [ "$GG_VERSION" = "latest" ]; then \
       GG_URL="https://github.com/grft-dev/graftcode-gateway/releases/latest/download/gg_linux_amd64.deb"; \
     else \
       GG_URL="https://github.com/grft-dev/graftcode-gateway/releases/download/v${GG_VERSION}/gg_linux_amd64.deb"; \
     fi \
  && wget -qO /tmp/gg.deb "$GG_URL" \
  && dpkg -i /tmp/gg.deb \
  && rm /tmp/gg.deb

WORKDIR /usr/app
COPY target/quarkus-app ./quarkus-app

# Minimal JAR for GMA/GPGE (facade + DTOs only — no HeroService/Uni on the publish surface).
RUN set -eux; \
  mkdir -p /tmp/publish/io/quarkus/sample/superheroes/hero/graft /tmp/publish/io/quarkus/sample/superheroes/hero/model; \
  cd /tmp/publish; \
  jar xf /usr/app/quarkus-app/app/rest-heroes-1.0.jar \
    io/quarkus/sample/superheroes/hero/graft/HeroesGraftFacade.class \
    io/quarkus/sample/superheroes/hero/model/HeroDto.class \
    io/quarkus/sample/superheroes/hero/model/HeroPatchDto.class; \
  printf 'Manifest-Version: 1.0\nImplementation-Title: rest-heroes\nImplementation-Version: 1.0\n' > /tmp/MANIFEST.MF; \
  jar cfm /usr/app/graft-publish.jar /tmp/MANIFEST.MF -C /tmp/publish .; \
  rm -rf /tmp/publish /tmp/MANIFEST.MF

EXPOSE 80
EXPOSE 81

CMD ["sh", "-c", "gg /usr/app/graft-publish.jar --graftOnly --types io.quarkus.sample.superheroes.hero.graft.HeroesGraftFacade --endpoint ${GG_ENDPOINT:-https://d.grft.dev}; \
  cd /usr/app/quarkus-app && java -jar quarkus-run.jar & \
  exec gg /usr/app/quarkus-app/app/rest-heroes-1.0.jar --runtime java \
  --types io.quarkus.sample.superheroes.hero.graft.HeroesGraftFacade \
  --endpoint ${GG_ENDPOINT:-https://d.grft.dev}"]
