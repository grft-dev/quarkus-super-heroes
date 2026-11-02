package io.quarkus.sample.superheroes.villain.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.sample.superheroes.villain.Villain;
import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.model.VillainPatchDto;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class VillainMapperTests {
  private static final int DTO_ID = 1;
  private static final Long ENTITY_ID = 1L;
  private static final String DEFAULT_NAME = "Super Chocolatine";
  private static final String DEFAULT_OTHER_NAME = "Super Chocolatine chocolate in";
  private static final String DEFAULT_PICTURE = "super_chocolatine.png";
  private static final String DEFAULT_POWERS = "does not eat pain au chocolat";
  private static final int DEFAULT_LEVEL = 42;

  @Inject
  VillainMapper villainMapper;

  @Test
  void toDto() {
    var entity = createEntity();
    var dto = this.villainMapper.toDto(entity);

    assertThat(dto)
      .isNotNull()
      .extracting(VillainDto::getId, VillainDto::getName, VillainDto::getOtherName, VillainDto::getLevel, VillainDto::getPicture, VillainDto::getPowers)
      .containsExactly(DTO_ID, DEFAULT_NAME, DEFAULT_OTHER_NAME, DEFAULT_LEVEL, DEFAULT_PICTURE, DEFAULT_POWERS);
  }

  @Test
  void toDtoNull() {
    assertThat(this.villainMapper.toDto(null))
      .isNull();
  }

  @Test
  void toDtoList() {
    var entities = List.of(createEntity());
    var dtos = this.villainMapper.toDtoList(entities);

    assertThat(dtos)
      .isNotNull()
      .singleElement()
      .extracting(VillainDto::getId, VillainDto::getName, VillainDto::getLevel)
      .containsExactly(DTO_ID, DEFAULT_NAME, DEFAULT_LEVEL);
  }

  @Test
  void toEntity() {
    var dto = new VillainDto()
      .setId(DTO_ID)
      .setName(DEFAULT_NAME)
      .setOtherName(DEFAULT_OTHER_NAME)
      .setLevel(DEFAULT_LEVEL)
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS);

    var entity = this.villainMapper.toEntity(dto);

    assertThat(entity)
      .isNotNull()
      .extracting("id", "name", "otherName", "level", "picture", "powers")
      .containsExactly(ENTITY_ID, DEFAULT_NAME, DEFAULT_OTHER_NAME, DEFAULT_LEVEL, DEFAULT_PICTURE, DEFAULT_POWERS);
  }

  @Test
  void toEntityNull() {
    assertThat(this.villainMapper.toEntity((VillainDto) null))
      .isNull();
  }

  @Test
  void toEntityList() {
    var dtos = List.of(
      new VillainDto()
        .setId(DTO_ID)
        .setName(DEFAULT_NAME)
        .setLevel(DEFAULT_LEVEL)
    );

    var entities = this.villainMapper.toEntityList(dtos);

    assertThat(entities)
      .isNotNull()
      .singleElement()
      .extracting("id", "name", "level")
      .containsExactly(ENTITY_ID, DEFAULT_NAME, DEFAULT_LEVEL);
  }

  @Test
  void toEntityFromPatch() {
    var patch = new VillainPatchDto()
      .setId(DTO_ID)
      .setName(DEFAULT_NAME)
      .setOtherName(DEFAULT_OTHER_NAME)
      .setLevel(DEFAULT_LEVEL)
      .setPicture(DEFAULT_PICTURE)
      .setPowers(DEFAULT_POWERS);

    var entity = this.villainMapper.toEntity(patch);

    assertThat(entity)
      .isNotNull()
      .extracting("id", "name", "otherName", "level", "picture", "powers")
      .containsExactly(ENTITY_ID, DEFAULT_NAME, DEFAULT_OTHER_NAME, DEFAULT_LEVEL, DEFAULT_PICTURE, DEFAULT_POWERS);
  }

  @Test
  void toEntityFromPatchNull() {
    assertThat(this.villainMapper.toEntity((VillainPatchDto) null))
      .isNull();
  }

  private Villain createEntity() {
    var villain = new Villain();
    villain.id = ENTITY_ID;
    villain.name = DEFAULT_NAME;
    villain.otherName = DEFAULT_OTHER_NAME;
    villain.level = DEFAULT_LEVEL;
    villain.picture = DEFAULT_PICTURE;
    villain.powers = DEFAULT_POWERS;
    return villain;
  }
}
