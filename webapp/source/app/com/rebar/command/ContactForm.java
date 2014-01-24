package com.rebar.command;

public class ContactForm {
  private String emailAddress;
  private String name;
  private String subject;
  private String message;
  private String website;
  
  public String getEmailAddress() {
    return emailAddress;
  }
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getWebsite() {
    return website;
  }
  public void setWebsite(String website) {
    this.website = website;
  }
}
