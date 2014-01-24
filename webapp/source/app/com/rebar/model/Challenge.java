package com.rebar.model;

public class Challenge {
  private Long challengeId;
  private String title;  
  private String description;
  private Long createdById;
  
  public Long getChallengeId() {
    return challengeId;
  }
  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Long getCreatedById() {
    return createdById;
  }
  public void setCreatedById(Long createdById) {
    this.createdById = createdById;
  }
  
}
