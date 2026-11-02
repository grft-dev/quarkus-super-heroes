package io.quarkus.sample.superheroes.hero.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Partial hero update for the Graftcode callable surface; all fields are optional.
 */
public class HeroPatchDto {

  /** {@code 0} means unset; use {@code int} per Graftcode portable baseline. */
  private int id;

  @Size(min = 3, max = 50)
  private String name;

  private String otherName;

  /** {@code 0} means omit level from the patch. */
  @Positive
  private int level;

  private String picture;

  private String powers;

  public int getId() {
    return id;
  }

  public HeroPatchDto setId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public HeroPatchDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getOtherName() {
    return otherName;
  }

  public HeroPatchDto setOtherName(String otherName) {
    this.otherName = otherName;
    return this;
  }

  public int getLevel() {
    return level;
  }

  public HeroPatchDto setLevel(int level) {
    this.level = level;
    return this;
  }

  public String getPicture() {
    return picture;
  }

  public HeroPatchDto setPicture(String picture) {
    this.picture = picture;
    return this;
  }

  public String getPowers() {
    return powers;
  }

  public HeroPatchDto setPowers(String powers) {
    this.powers = powers;
    return this;
  }
}
