package com.rebar.model;

public class HighlightedPoint extends Point {
  private Long highlightedPointId;
  private Boolean readFlag;

  public Long getHighlightedPointId() {
    return highlightedPointId;
  }

  public void setHighlightedPointId(Long highlightedPointId) {
    this.highlightedPointId = highlightedPointId;
  }

  /**
   * @return the readFlag
   */
  public Boolean getReadFlag() {
    return readFlag;
  }

  /**
   * @param readFlag the readFlag to set
   */
  public void setReadFlag(Boolean readFlag) {
    this.readFlag = readFlag;
  }

}
