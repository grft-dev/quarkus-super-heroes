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

ENV GRAFT_FACADE_TYPE=io.quarkus.sample.superheroes.fight.facade.FightsGraftFacade
ENV GRAFT_FIGHT_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightDto
ENV GRAFT_FIGHT_LOCATION_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightLocationDto
ENV GRAFT_FIGHTER_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FighterDto
ENV GRAFT_FIGHT_REQUEST_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightRequestDto
ENV GRAFT_FIGHT_TO_NARRATE_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightToNarrateDto
ENV GRAFT_FIGHT_TO_NARRATE_LOCATION_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightToNarrateLocationDto
ENV GRAFT_IMAGE_GENERATION_REQUEST_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.ImageGenerationRequestDto
ENV GRAFT_FIGHT_IMAGE_DTO_TYPE=io.quarkus.sample.superheroes.fight.model.FightImageDto
ENV GRAFT_TYPES=${GRAFT_FACADE_TYPE},${GRAFT_FIGHT_DTO_TYPE},${GRAFT_FIGHT_LOCATION_DTO_TYPE},${GRAFT_FIGHTER_DTO_TYPE},${GRAFT_FIGHT_REQUEST_DTO_TYPE},${GRAFT_FIGHT_TO_NARRATE_DTO_TYPE},${GRAFT_FIGHT_TO_NARRATE_LOCATION_DTO_TYPE},${GRAFT_IMAGE_GENERATION_REQUEST_DTO_TYPE},${GRAFT_FIGHT_IMAGE_DTO_TYPE}

RUN set -eux; \
  mkdir -p /tmp/publish/io/quarkus/sample/superheroes/fight/facade /tmp/publish/io/quarkus/sample/superheroes/fight/model; \
  cd /tmp/publish; \
  jar xf /usr/app/quarkus-app/app/rest-fights-1.0.0.0.jar \
    io/quarkus/sample/superheroes/fight/facade/FightsGraftFacade.class \
    io/quarkus/sample/superheroes/fight/model/FightDto.class \
    io/quarkus/sample/superheroes/fight/model/FightLocationDto.class \
    io/quarkus/sample/superheroes/fight/model/FighterDto.class \
    io/quarkus/sample/superheroes/fight/model/FightRequestDto.class \
    io/quarkus/sample/superheroes/fight/model/FightToNarrateDto.class \
    io/quarkus/sample/superheroes/fight/model/FightToNarrateLocationDto.class \
    io/quarkus/sample/superheroes/fight/model/ImageGenerationRequestDto.class \
    io/quarkus/sample/superheroes/fight/model/FightImageDto.class; \
  printf 'Manifest-Version: 1.0\nImplementation-Title: rest-fights\nImplementation-Version: 1.0.0.0\n' > /tmp/MANIFEST.MF; \
  jar cfm /usr/app/graft-publish.jar /tmp/MANIFEST.MF -C /tmp/publish .; \
  rm -rf /tmp/publish /tmp/MANIFEST.MF

EXPOSE 80
EXPOSE 81

CMD ["sh", "-c", "gg /usr/app/graft-publish.jar --graftOnly --types ${GRAFT_TYPES} --endpoint ${GG_ENDPOINT:-https://d.grft.dev}; \
  cd /usr/app/quarkus-app && java -jar quarkus-run.jar & \
  exec gg /usr/app/quarkus-app/app/rest-fights-1.0.0.0.jar --runtime java \
  --types ${GRAFT_TYPES} \
  --endpoint ${GG_ENDPOINT:-https://d.grft.dev}"]
