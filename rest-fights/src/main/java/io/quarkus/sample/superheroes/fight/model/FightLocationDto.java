package io.quarkus.sample.superheroes.fight.model;

/** Location of a fight on the Graftcode callable surface. */
public class FightLocationDto {

  private String name;
  private String description;
  private String picture;

  public String getName() {
    return name;
  }

  public FightLocationDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public FightLocationDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getPicture() {
    return picture;
  }

  public FightLocationDto setPicture(String picture) {
    this.picture = picture;
    return this;
  }
}
