package com.rebar.model;

/**
 * @author Dan Kelley
 */
public class Role {
  
  public static final long ROLE_OWNER = 1;
  public static final long ROLE_EDITOR = 2;
  public static final long ROLE_ASSISTANT = 3;
  
  private Long roleId;
  private String description;

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}