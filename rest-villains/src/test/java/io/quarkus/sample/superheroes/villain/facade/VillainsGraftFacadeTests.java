package io.quarkus.sample.superheroes.villain.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.service.VillainService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class VillainsGraftFacadeTests {

  private static final int DEFAULT_ID = 1;
  private static final Long ENTITY_ID = 1L;

  @Inject
  VillainsGraftFacade facade;

  @InjectMock
  VillainService villainService;

  @Test
  void helloDoesNotCallService() {
    assertThat(facade.hello()).isEqualTo(VillainService.HELLO_MESSAGE);
    verifyNoInteractions(villainService);
  }

  @Test
  void shouldNotGetUnknownVillain() {
    when(villainService.findVillainApiById(ENTITY_ID))
      .thenReturn(Optional.empty());

    assertThat(facade.getVillain(DEFAULT_ID)).isNull();

    verify(villainService).findVillainApiById(ENTITY_ID);
  }

  @Test
  void shouldGetRandomVillainNotFound() {
    when(villainService.findRandomVillainApi())
      .thenReturn(Optional.empty());

    assertThat(facade.getRandomVillain()).isNull();

    verify(villainService).findRandomVillainApi();
  }

  @Test
  void shouldListVillainsWithFilter() {
    var dto = new VillainDto().setId(DEFAULT_ID).setName("name").setLevel(10);
    when(villainService.listVillains("name"))
      .thenReturn(List.of(dto));

    assertThat(facade.getAllVillains("name"))
      .singleElement()
      .extracting(VillainDto::getName)
      .isEqualTo("name");

    verify(villainService).listVillains("name");
  }
}
