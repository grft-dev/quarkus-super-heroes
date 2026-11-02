package io.quarkus.sample.superheroes.fight.model;

/** Request to perform a fight on the Graftcode callable surface. */
public class FightRequestDto {

  private FighterDto hero = new FighterDto();
  private FighterDto villain = new FighterDto();
  private FightLocationDto location = new FightLocationDto();

  public FighterDto getHero() {
    return hero;
  }

  public FightRequestDto setHero(FighterDto hero) {
    this.hero = hero;
    return this;
  }

  public FighterDto getVillain() {
    return villain;
  }

  public FightRequestDto setVillain(FighterDto villain) {
    this.villain = villain;
    return this;
  }

  public FightLocationDto getLocation() {
    return location;
  }

  public FightRequestDto setLocation(FightLocationDto location) {
    this.location = location;
    return this;
  }
}
