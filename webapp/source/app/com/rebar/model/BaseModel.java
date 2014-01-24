package com.rebar.model;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rebar.web.controller.api.AccountApiController;

public abstract class BaseModel {
  protected Log logger = LogFactory.getLog(AccountApiController.class);
  
  private Date createdDate;
  private Date updatedDate;

  /**
   * @return the createdDate
   */
  public Date getCreatedDate() {
    return createdDate;
  }

  /**
   * @param createdDate the createdDate to set
   */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * @return the updatedDate
   */
  public Date getUpdatedDate() {
    return updatedDate;
  }

  /**
   * @param updatedDate the updatedDate to set
   */
  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

}
