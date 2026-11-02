package io.quarkus.sample.superheroes.fight.model;

/** Fight outcome passed to narration on the Graftcode callable surface. */
public class FightToNarrateDto {

  private String winnerTeam;
  private String winnerName;
  private String winnerPowers;
  private int winnerLevel;
  private String loserTeam;
  private String loserName;
  private String loserPowers;
  private int loserLevel;
  private FightToNarrateLocationDto location = new FightToNarrateLocationDto();

  public String getWinnerTeam() {
    return winnerTeam;
  }

  public FightToNarrateDto setWinnerTeam(String winnerTeam) {
    this.winnerTeam = winnerTeam;
    return this;
  }

  public String getWinnerName() {
    return winnerName;
  }

  public FightToNarrateDto setWinnerName(String winnerName) {
    this.winnerName = winnerName;
    return this;
  }

  public String getWinnerPowers() {
    return winnerPowers;
  }

  public FightToNarrateDto setWinnerPowers(String winnerPowers) {
    this.winnerPowers = winnerPowers;
    return this;
  }

  public int getWinnerLevel() {
    return winnerLevel;
  }

  public FightToNarrateDto setWinnerLevel(int winnerLevel) {
    this.winnerLevel = winnerLevel;
    return this;
  }

  public String getLoserTeam() {
    return loserTeam;
  }

  public FightToNarrateDto setLoserTeam(String loserTeam) {
    this.loserTeam = loserTeam;
    return this;
  }

  public String getLoserName() {
    return loserName;
  }

  public FightToNarrateDto setLoserName(String loserName) {
    this.loserName = loserName;
    return this;
  }

  public String getLoserPowers() {
    return loserPowers;
  }

  public FightToNarrateDto setLoserPowers(String loserPowers) {
    this.loserPowers = loserPowers;
    return this;
  }

  public int getLoserLevel() {
    return loserLevel;
  }

  public FightToNarrateDto setLoserLevel(int loserLevel) {
    this.loserLevel = loserLevel;
    return this;
  }

  public FightToNarrateLocationDto getLocation() {
    return location;
  }

  public FightToNarrateDto setLocation(FightToNarrateLocationDto location) {
    this.location = location;
    return this;
  }
}
