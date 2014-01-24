package com.rebar.model;

public class Point extends BaseModel {
  private Long pointId;
  private Long trakId;
  private boolean highlighted;
  private boolean readFlag;
  private Long createdBy;
  private String title;
  private String description;

  public Long getPointId() {
    return pointId;
  }

  public void setPointId(Long pointId) {
    this.pointId = pointId;
  }

  public Long getTrakId() {
    return trakId;
  }

  public void setTrakId(Long trakId) {
    this.trakId = trakId;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the highlighted
   */
  public boolean isHighlighted() {
    return highlighted;
  }

  /**
   * @param highlighted the highlighted to set
   */
  public void setHighlighted(boolean highlighted) {
    this.highlighted = highlighted;
  }

  /**
   * @return the readFlag
   */
  public boolean isReadFlag() {
    return readFlag;
  }

  /**
   * @param readFlag the readFlag to set
   */
  public void setReadFlag(boolean readFlag) {
    this.readFlag = readFlag;
  }

}
