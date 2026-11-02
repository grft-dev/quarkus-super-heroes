package io.quarkus.sample.superheroes.villain.facade;

import java.util.Arrays;
import java.util.List;

import io.quarkus.arc.Arc;
import io.quarkus.sample.superheroes.villain.model.VillainDto;
import io.quarkus.sample.superheroes.villain.model.VillainPatchDto;
import io.quarkus.sample.superheroes.villain.service.VillainService;

/** Internal blocking bridge; loaded at runtime via reflection from {@link VillainsGraftFacade}. */
public final class VillainsGraftExecutor {

  private VillainsGraftExecutor() {}

  public static VillainDto getRandomVillain() {
    return villainService().findRandomVillainApi().orElse(null);
  }

  public static VillainDto[] getAllVillains(String nameFilter) {
    List<VillainDto> list = villainService().listVillains(nameFilter);
    return list.toArray(VillainDto[]::new);
  }

  public static VillainDto getVillain(int id) {
    return villainService().findVillainApiById(toEntityId(id)).orElse(null);
  }

  public static int createVillain(VillainDto villain) {
    return villainService().createVillain(villain).getId();
  }

  public static boolean replaceVillain(int id, VillainDto villain) {
    return villainService().replaceVillain(toEntityId(id), villain);
  }

  public static VillainDto partialUpdateVillain(int id, VillainPatchDto patch) {
    return villainService().partialUpdateVillain(toEntityId(id), patch).orElse(null);
  }

  public static void deleteVillain(int id) {
    villainService().deleteVillain(toEntityId(id));
  }

  public static void deleteAllVillains() {
    villainService().deleteAllVillains();
  }

  public static void replaceAllVillains(VillainDto[] villains) {
    villainService().replaceAllVillainsFromDtos(Arrays.asList(villains));
  }

  private static Long toEntityId(int id) {
    return id == 0 ? null : (long) id;
  }

  private static VillainService villainService() {
    return Arc.container().instance(VillainService.class).get();
  }
}
