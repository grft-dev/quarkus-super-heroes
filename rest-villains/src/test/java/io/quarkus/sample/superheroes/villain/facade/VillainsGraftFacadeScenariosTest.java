package io.quarkus.sample.superheroes.villain.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.model.VillainPatchDto;
import io.quarkus.sample.superheroes.villain.service.VillainService;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class VillainsGraftFacadeScenariosTest {

  private static final int DEFAULT_ORDER = 0;
  private static final String DEFAULT_NAME = "Super Chocolatine";
  private static final String UPDATED_NAME = DEFAULT_NAME + " (updated)";
  private static final String DEFAULT_OTHER_NAME = "Super Chocolatine chocolate in";
  private static final String UPDATED_OTHER_NAME = DEFAULT_OTHER_NAME + " (updated)";
  private static final String DEFAULT_PICTURE = "super_chocolatine.png";
  private static final String UPDATED_PICTURE = "super_chocolatine_updated.png";
  private static final String DEFAULT_POWERS = "does not eat pain au chocolat";
  private static final String UPDATED_POWERS = DEFAULT_POWERS + " (updated)";
  private static final int DEFAULT_LEVEL = 42;
  private static final int UPDATED_LEVEL = DEFAULT_LEVEL + 1;

  private static final int NB_VILLAINS = 100;
  private static int villainId;

  @Inject
  VillainsGraftFacade facade;

  @Test
  @Order(DEFAULT_ORDER)
  void hello() {
    assertThat(facade.hello()).isEqualTo(VillainService.HELLO_MESSAGE);
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotGetUnknownVillain() {
    assertThat(facade.getVillain(Integer.MAX_VALUE - new Random().nextInt(10_000))).isNull();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldGetRandomVillainFound() {
    assertThat(facade.getRandomVillain()).isNotNull();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotAddInvalidItem() {
    var villain = new VillainDto()
      .setName(null)
      .setOtherName(DEFAULT_OTHER_NAME)
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS)
      .setLevel(0);

    assertThatThrownBy(() -> facade.createVillain(villain))
      .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotFullyUpdateInvalidItem() {
    var villain = new VillainDto()
      .setId(1)
      .setName(null)
      .setOtherName(UPDATED_OTHER_NAME)
      .setPicture(UPDATED_PICTURE)
      .setPowers(UPDATED_PICTURE)
      .setLevel(0);

    assertThatThrownBy(() -> facade.replaceVillain(villain.getId(), villain))
      .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotPartiallyUpdateInvalidItem() {
    var patch = new VillainPatchDto()
      .setId(50)
      .setName(null)
      .setOtherName(UPDATED_OTHER_NAME)
      .setPicture(UPDATED_PICTURE)
      .setPowers(UPDATED_PICTURE)
      .setLevel(-1);

    assertThatThrownBy(() -> facade.partialUpdateVillain(patch.getId(), patch))
      .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotFullyUpdateNotFoundItem() {
    var villain = new VillainDto()
      .setId(-1)
      .setName(UPDATED_NAME)
      .setOtherName(UPDATED_OTHER_NAME)
      .setPicture(UPDATED_PICTURE)
      .setPowers(UPDATED_POWERS)
      .setLevel(UPDATED_LEVEL);

    assertThat(facade.replaceVillain(-1, villain)).isFalse();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotPartiallyUpdateNotFoundItem() {
    var patch = new VillainPatchDto()
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS);

    assertThat(facade.partialUpdateVillain(-1, patch)).isNull();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldNotGetAnyVillainsThatDontMatchFilterCriteria() {
    assertThat(facade.getAllVillains("iooi90904890358349 8890re9ierkjlk;sdf098w459idxflkjdfjoiio4ue"))
      .isEmpty();
  }

  @Test
  @Order(DEFAULT_ORDER)
  void shouldGetVillainsThatMatchFilterCriteria() {
    var villains = facade.getAllVillains("darth");

    assertThat(villains)
      .hasSize(2)
      .extracting(VillainDto::getName)
      .containsExactly("Darth Sidious", "Darth Vader");
  }

  @Test
  @Order(DEFAULT_ORDER + 1)
  void shouldGetInitialItems() {
    assertThat(facade.getAllVillains(null)).hasSize(NB_VILLAINS);
  }

  @Test
  @Order(DEFAULT_ORDER + 2)
  void shouldAddAnItem() {
    var villain = createDefaultVillainDto();

    villainId = facade.createVillain(villain);
    assertThat(villainId).isPositive();

    var returnedVillain = facade.getVillain(villainId);
    assertThat(returnedVillain).isNotNull();
    assertThat(returnedVillain)
      .usingRecursiveComparison()
      .ignoringFields("id")
      .isEqualTo(villain);

    assertThat(facade.getAllVillains(null)).hasSize(NB_VILLAINS + 1);
  }

  @Test
  @Order(DEFAULT_ORDER + 3)
  void shouldFullyUpdateAnItem() {
    var villain = new VillainDto()
      .setId(villainId)
      .setName(UPDATED_NAME)
      .setOtherName(UPDATED_OTHER_NAME)
      .setPicture(UPDATED_PICTURE)
      .setPowers(UPDATED_POWERS)
      .setLevel(UPDATED_LEVEL);

    assertThat(facade.replaceVillain(villainId, villain)).isTrue();
    assertThat(facade.getAllVillains(null)).hasSize(NB_VILLAINS + 1);

    var updatedVillain = facade.getVillain(villainId);
    assertThat(updatedVillain).isNotNull();
    assertThat(updatedVillain)
      .usingRecursiveComparison()
      .isEqualTo(villain);
  }

  @Test
  @Order(DEFAULT_ORDER + 4)
  void shouldPartiallyUpdateAnItem() {
    var patch = new VillainPatchDto()
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS);

    var expectedVillain = new VillainDto()
      .setName(UPDATED_NAME)
      .setOtherName(UPDATED_OTHER_NAME)
      .setPicture(patch.getPicture())
      .setPowers(patch.getPowers())
      .setLevel(UPDATED_LEVEL)
      .setId(villainId);

    var patchedVillain = facade.partialUpdateVillain(villainId, patch);
    assertThat(patchedVillain).isNotNull();

    assertThat(patchedVillain)
      .usingRecursiveComparison()
      .isEqualTo(expectedVillain);

    assertThat(facade.getAllVillains(null)).hasSize(NB_VILLAINS + 1);
  }

  @Test
  @Order(DEFAULT_ORDER + 5)
  void shouldDeleteVillain() {
    facade.deleteVillain(villainId);
    assertThat(facade.getAllVillains(null)).hasSize(NB_VILLAINS);
  }

  @Test
  @Order(DEFAULT_ORDER + 6)
  void shouldDeleteAllVillains() {
    facade.deleteAllVillains();
    assertThat(facade.getAllVillains(null)).isEmpty();
  }

  @Test
  @Order(DEFAULT_ORDER + 7)
  void shouldGetRandomVillainNotFound() {
    assertThat(facade.getRandomVillain()).isNull();
  }

  @Test
  @Order(DEFAULT_ORDER + 8)
  void shouldReplaceAllVillains() {
    var v1 = createDefaultVillainDto();
    var v2 = createUpdatedVillainDto();

    facade.createVillain(v1);
    assertThat(facade.getAllVillains(null)).hasSize(1);

    facade.replaceAllVillains(new VillainDto[] {v1, v2});

    var villains = facade.getAllVillains(null);
    assertThat(villains)
      .hasSize(2)
      .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
      .containsExactly(createDefaultVillainDto(), createUpdatedVillainDto());
  }

  private static VillainDto createDefaultVillainDto() {
    return new VillainDto()
      .setName(DEFAULT_NAME)
      .setOtherName(DEFAULT_OTHER_NAME)
      .setLevel(DEFAULT_LEVEL)
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS);
  }

  private static VillainDto createUpdatedVillainDto() {
    return new VillainDto()
      .setName(UPDATED_NAME)
      .setOtherName(UPDATED_OTHER_NAME)
      .setLevel(UPDATED_LEVEL)
      .setPicture(UPDATED_PICTURE)
      .setPowers(UPDATED_POWERS);
  }
}
