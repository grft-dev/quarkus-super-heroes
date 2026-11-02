package io.quarkus.sample.superheroes.villain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Villain data exposed on the Graftcode callable surface (no JPA or HTTP types).
 */
public class VillainDto {

  /** {@code 0} means unset (e.g. on create); use {@code int} per Graftcode portable baseline. */
  private int id;

  @NotNull
  @Size(min = 3, max = 50)
  private String name;

  private String otherName;

  @Positive
  private int level;

  private String picture;

  private String powers;

  public int getId() {
    return id;
  }

  public VillainDto setId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public VillainDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getOtherName() {
    return otherName;
  }

  public VillainDto setOtherName(String otherName) {
    this.otherName = otherName;
    return this;
  }

  public int getLevel() {
    return level;
  }

  public VillainDto setLevel(int level) {
    this.level = level;
    return this;
  }

  public String getPicture() {
    return picture;
  }

  public VillainDto setPicture(String picture) {
    this.picture = picture;
    return this;
  }

  public String getPowers() {
    return powers;
  }

  public VillainDto setPowers(String powers) {
    this.powers = powers;
    return this;
  }
}
