package io.quarkus.sample.superheroes.hero.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import io.quarkus.sample.superheroes.hero.Hero;
import io.quarkus.sample.superheroes.hero.model.HeroDto;
import io.quarkus.sample.superheroes.hero.model.HeroPatchDto;

@Mapper(componentModel = ComponentModel.JAKARTA_CDI)
public interface HeroMapper {

  @Mapping(target = "id", expression = "java(toDtoId(entity.getId()))")
  HeroDto toDto(Hero entity);

  List<HeroDto> toDtoList(List<Hero> entities);

  @Mapping(target = "id", expression = "java(toEntityId(dto.getId()))")
  Hero toEntity(HeroDto dto);

  List<Hero> toEntityList(List<HeroDto> dtos);

  @Mapping(target = "id", expression = "java(toEntityId(patch.getId()))")
  @Mapping(target = "level", expression = "java(patchLevelToEntity(patch.getLevel()))")
  Hero toEntity(HeroPatchDto patch);

  default int toDtoId(Long id) {
    return id == null ? 0 : Math.toIntExact(id);
  }

  default Long toEntityId(int id) {
    return id == 0 ? null : (long) id;
  }

  default Integer patchLevelToEntity(int level) {
    return level == 0 ? null : level;
  }
}
