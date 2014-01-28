package com.rebar.service;

import static org.apache.commons.lang.StringUtils.trim;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.List;

import com.google.inject.Inject;
import com.rebar.command.ContactForm;
import com.rebar.model.Account;
import com.xmog.stack.db.Database;
import com.xmog.stack.service.StackAccountService;
import com.xmog.stack.util.Encryptor;

/**
 * @author Dan Kelley
 */
public class AccountService implements StackAccountService<Account, Long, Long> {  
  private Database database;
  private Encryptor encryptor;

  @Inject
  public AccountService(Database database, Encryptor encryptor) {
    this.database = database;
    this.encryptor = encryptor;
  }
  
  public Account findAccountByEmailAddressAndPassword(String emailAddress, String password) {
    
    emailAddress = trimToEmpty(emailAddress).toLowerCase();
    return database.queryForObject("select * from account where email=? and password =?", Account.class, trim(emailAddress), encryptor.hashSecurely(trimToEmpty(password)));

  }  

  public Account findAccountByApiToken(String apiToken) {
    return database.queryForObject("select * from account where api_token=?", Account.class, trim(apiToken));
  }

  public Account findAccountByRememberMeToken(String rememberMeToken) {
    return database.queryForObject("select * from account where remember_me_token=?", Account.class, trim(rememberMeToken));
  }

  public List<Long> findRolesByAccountId(Long accountId) {
    return database.queryForList("select role_id from account_role_trak where account_id=?", Long.class, accountId);
  }  
  
  public Long saveContactForm(ContactForm contactForm) {
    return database.executeInsert("insert into registration(registration_id, name, email_address, subject, website, message) values(nextval('registration_seq'), ?,?,?,?,?) RETURNING registration_id", Long.class, contactForm.getName(), contactForm.getEmailAddress(), contactForm.getSubject(), contactForm.getWebsite(), contactForm.getMessage());
  }    
}
