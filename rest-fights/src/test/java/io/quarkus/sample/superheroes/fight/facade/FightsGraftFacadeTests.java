package io.quarkus.sample.superheroes.fight.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.sample.superheroes.fight.Fight;
import io.quarkus.sample.superheroes.fight.FightLocation;
import io.quarkus.sample.superheroes.fight.service.FightService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import io.smallrye.mutiny.Uni;

@QuarkusTest
class FightsGraftFacadeTests {

  @Inject
  FightsGraftFacade facade;

  @InjectMock
  FightService fightService;

  @Test
  void helloDoesNotCallService() {
    assertThat(facade.hello()).isEqualTo(FightsGraftFacade.HELLO_MESSAGE);
    verifyNoInteractions(fightService);
  }

  @Test
  void getFightsMapsServiceResults() {
    var fight = sampleFight();
    when(fightService.findFights(0, 5)).thenReturn(Uni.createFrom().item(List.of(fight)));

    var result = facade.getFights(0, 5);

    assertThat(result).hasSize(1);
    assertThat(result[0].getWinnerName()).isEqualTo("Hero");
    assertThat(result[0].getLoserName()).isEqualTo("Villain");
    verify(fightService).findFights(0, 5);
  }

  @Test
  void getRandomLocationMapsServiceResult() {
    var location = new FightLocation("Arena", "Dark pit", "https://example.com/pit.png");
    when(fightService.findRandomLocation()).thenReturn(Uni.createFrom().item(location));

    var dto = facade.getRandomLocation();

    assertThat(dto.getName()).isEqualTo("Arena");
    assertThat(dto.getDescription()).isEqualTo("Dark pit");
    verify(fightService).findRandomLocation();
  }

  private static Fight sampleFight() {
    var fight = new Fight();
    fight.fightDate = Instant.parse("2026-01-01T12:00:00Z");
    fight.winnerName = "Hero";
    fight.winnerLevel = 10;
    fight.winnerPowers = "fly";
    fight.winnerPicture = "https://hero.png";
    fight.loserName = "Villain";
    fight.loserLevel = 9;
    fight.loserPowers = "evil";
    fight.loserPicture = "https://villain.png";
    fight.winnerTeam = "heroes";
    fight.loserTeam = "villains";
    fight.location = new FightLocation("Arena", "Dark pit", "https://example.com/pit.png");
    return fight;
  }
}
