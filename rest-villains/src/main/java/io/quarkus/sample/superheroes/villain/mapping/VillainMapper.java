package io.quarkus.sample.superheroes.villain.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import io.quarkus.sample.superheroes.villain.Villain;
import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.model.VillainPatchDto;

@Mapper(componentModel = ComponentModel.JAKARTA_CDI)
public interface VillainMapper {

  @Mapping(target = "id", expression = "java(toDtoId(entity.id))")
  VillainDto toDto(Villain entity);

  List<VillainDto> toDtoList(List<Villain> entities);

  @Mapping(target = "id", expression = "java(toEntityId(dto.getId()))")
  Villain toEntity(VillainDto dto);

  List<Villain> toEntityList(List<VillainDto> dtos);

  @Mapping(target = "id", expression = "java(toEntityId(patch.getId()))")
  @Mapping(target = "level", expression = "java(patchLevelToEntity(patch.getLevel()))")
  Villain toEntity(VillainPatchDto patch);

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
