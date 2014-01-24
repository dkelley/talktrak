package com.rebar.model;

public class TrakDetail extends Trak {
  private Long roleId;
  private String role;
  
  // info for person who created it
  private Long accountId;
  private String email;
  private String username;
  private Long collaboratorCnt;

  /**
   * @return the roleId
   */
  public Long getRoleId() {
    return roleId;
  }

  /**
   * @param roleId the roleId to set
   */
  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  /**
   * @return the accountId
   */
  public Long getAccountId() {
    return accountId;
  }

  /**
   * @param accountId the accountId to set
   */
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the roleName
   */
  public String getRole() {
    return role;
  }

  /**
   * @param roleName the roleName to set
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * @return the collaboratorsCnt
   */
  public Long getCollaboratorCnt() {
    return collaboratorCnt;
  }

  /**
   * @param collaboratorsCnt the collaboratorsCnt to set
   */
  public void setCollaboratorCnt(Long collaboratorCnt) {
    this.collaboratorCnt = collaboratorCnt;
  }

  
}
