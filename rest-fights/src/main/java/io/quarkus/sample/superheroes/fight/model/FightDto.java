package io.quarkus.sample.superheroes.fight.model;

/**
 * Persisted fight on the Graftcode callable surface (no Mongo or HTTP types).
 */
public class FightDto {

  private String id;
  /** ISO-8601 date-time string. */
  private String fightDate;
  private String winnerName;
  private int winnerLevel;
  private String winnerPowers;
  private String winnerPicture;
  private String loserName;
  private int loserLevel;
  private String loserPowers;
  private String loserPicture;
  private String winnerTeam;
  private String loserTeam;
  private FightLocationDto location = new FightLocationDto();

  public String getId() {
    return id;
  }

  public FightDto setId(String id) {
    this.id = id;
    return this;
  }

  public String getFightDate() {
    return fightDate;
  }

  public FightDto setFightDate(String fightDate) {
    this.fightDate = fightDate;
    return this;
  }

  public String getWinnerName() {
    return winnerName;
  }

  public FightDto setWinnerName(String winnerName) {
    this.winnerName = winnerName;
    return this;
  }

  public int getWinnerLevel() {
    return winnerLevel;
  }

  public FightDto setWinnerLevel(int winnerLevel) {
    this.winnerLevel = winnerLevel;
    return this;
  }

  public String getWinnerPowers() {
    return winnerPowers;
  }

  public FightDto setWinnerPowers(String winnerPowers) {
    this.winnerPowers = winnerPowers;
    return this;
  }

  public String getWinnerPicture() {
    return winnerPicture;
  }

  public FightDto setWinnerPicture(String winnerPicture) {
    this.winnerPicture = winnerPicture;
    return this;
  }

  public String getLoserName() {
    return loserName;
  }

  public FightDto setLoserName(String loserName) {
    this.loserName = loserName;
    return this;
  }

  public int getLoserLevel() {
    return loserLevel;
  }

  public FightDto setLoserLevel(int loserLevel) {
    this.loserLevel = loserLevel;
    return this;
  }

  public String getLoserPowers() {
    return loserPowers;
  }

  public FightDto setLoserPowers(String loserPowers) {
    this.loserPowers = loserPowers;
    return this;
  }

  public String getLoserPicture() {
    return loserPicture;
  }

  public FightDto setLoserPicture(String loserPicture) {
    this.loserPicture = loserPicture;
    return this;
  }

  public String getWinnerTeam() {
    return winnerTeam;
  }

  public FightDto setWinnerTeam(String winnerTeam) {
    this.winnerTeam = winnerTeam;
    return this;
  }

  public String getLoserTeam() {
    return loserTeam;
  }

  public FightDto setLoserTeam(String loserTeam) {
    this.loserTeam = loserTeam;
    return this;
  }

  public FightLocationDto getLocation() {
    return location;
  }

  public FightDto setLocation(FightLocationDto location) {
    this.location = location;
    return this;
  }
}
