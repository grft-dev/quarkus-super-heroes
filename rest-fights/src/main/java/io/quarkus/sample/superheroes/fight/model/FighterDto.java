package io.quarkus.sample.superheroes.fight.model;

/**
 * Hero or villain fighter data on the Graftcode callable surface.
 */
public class FighterDto {

  private String name;
  private int level;
  private String picture;
  private String powers;

  public String getName() {
    return name;
  }

  public FighterDto setName(String name) {
    this.name = name;
    return this;
  }

  public int getLevel() {
    return level;
  }

  public FighterDto setLevel(int level) {
    this.level = level;
    return this;
  }

  public String getPicture() {
    return picture;
  }

  public FighterDto setPicture(String picture) {
    this.picture = picture;
    return this;
  }

  public String getPowers() {
    return powers;
  }

  public FighterDto setPowers(String powers) {
    this.powers = powers;
    return this;
  }
}
