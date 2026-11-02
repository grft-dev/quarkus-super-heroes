package io.quarkus.sample.superheroes.fight.model;

/** Fight location snippet for narration on the Graftcode callable surface. */
public class FightToNarrateLocationDto {

  private String name;
  private String description;

  public String getName() {
    return name;
  }

  public FightToNarrateLocationDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public FightToNarrateLocationDto setDescription(String description) {
    this.description = description;
    return this;
  }
}
