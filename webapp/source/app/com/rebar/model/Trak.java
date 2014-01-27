package com.rebar.model;

import org.apache.commons.lang.StringUtils;

public class Trak extends BaseModel{
    private Long trakId;
    private String title;  
    private String trakUrl;  
    private String description;
    private boolean active;
    private boolean inUse;
    
    public Long getTrakId() {
      return trakId;
    }
    public void setTrakId(Long trakId) {
      this.trakId = trakId;
    }
    
    public String getTitle() {
      if (!StringUtils.isEmpty(title))
        return title;
      else
        return "No title";
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
    /**
     * @return the active
     */
    public boolean isActive() {
      return active;
    }
    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
      this.active = active;
    }
    /**
     * @return the inUse
     */
    public boolean isInUse() {
      return inUse;
    }
    /**
     * @param inUse the inUse to set
     */
    public void setInUse(boolean inUse) {
      this.inUse = inUse;
    }
	public String getTrakUrl() {
		return trakUrl;
	}
	public void setTrakUrl(String trakUrl) {
		this.trakUrl = trakUrl;
	}
 
  }
