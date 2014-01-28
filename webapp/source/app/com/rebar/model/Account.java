package com.rebar.model;

import static com.google.common.base.Objects.toStringHelper;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.base.Objects.ToStringHelper;
import com.xmog.stack.web.context.StackAccount;

/**
 * @author Dan Kelley
 */
public class Account  implements StackAccount<Long> {
  private Long accountId;
  private String email;
  private String apiToken;
  private String rememberMeToken;
  private Map<Long, Long> roleForTrak;

  public String getAccountDescription() {
    ToStringHelper toStringHelper = toStringHelper(this);
    toStringHelper.add("accountId", getAccountId());  
    toStringHelper.add("emailAddress", getEmail());
    return toStringHelper.toString();
  }

  @Override
  public String toString() {
    return getAccountDescription();
  }

  public Long getAccountId() {
    return accountId;
  }
  
  public Long getRoleForTrak(Long trakId){
    return Role.ROLE_OWNER;
  }
  
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }  

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }  
  
  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public String getRememberMeToken() {
    return rememberMeToken;
  }

  public void setRememberMeToken(String rememberMeToken) {
    this.rememberMeToken = rememberMeToken;
  }

  @Override
  public TimeZone getTimeZone() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Locale getLocale() {
    // TODO Auto-generated method stub
    return null;
  }
}