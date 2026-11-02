package io.quarkus.sample.superheroes.fight.model;

/** Image generation request on the Graftcode callable surface. */
public class ImageGenerationRequestDto {

  private String narration;
  private String winnerPictureUrl;
  private String loserPictureUrl;

  public String getNarration() {
    return narration;
  }

  public ImageGenerationRequestDto setNarration(String narration) {
    this.narration = narration;
    return this;
  }

  public String getWinnerPictureUrl() {
    return winnerPictureUrl;
  }

  public ImageGenerationRequestDto setWinnerPictureUrl(String winnerPictureUrl) {
    this.winnerPictureUrl = winnerPictureUrl;
    return this;
  }

  public String getLoserPictureUrl() {
    return loserPictureUrl;
  }

  public ImageGenerationRequestDto setLoserPictureUrl(String loserPictureUrl) {
    this.loserPictureUrl = loserPictureUrl;
    return this;
  }
}
