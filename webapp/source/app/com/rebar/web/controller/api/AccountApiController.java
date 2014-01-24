package com.rebar.web.controller.api;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.rebar.command.AccountSignInCommand;
import com.rebar.command.ContactForm;
import com.rebar.model.Account;
import com.rebar.service.AccountService;
import com.rebar.web.context.CurrentContext;
import com.xmog.stack.web.annotation.Public;
import com.xmog.stack.exception.AuthenticationException;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/api/accounts")
@Produces(CONTENT_TYPE_JSON)
public class AccountApiController {
  private Provider<CurrentContext> currentContextProvider;
  private AccountService accountService;
  
  private Log log = LogFactory.getLog(AccountApiController.class);

  @Inject
  public AccountApiController(Provider<CurrentContext> currentContextProvider, AccountService accountService) {
    this.currentContextProvider = currentContextProvider;
    this.accountService = accountService;
  }

  @POST
  @Public
  @Path("sign-in")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> signIn(AccountSignInCommand command) {
    final Account account =
        accountService.findAccountByEmailAddressAndPassword(command.getEmailAddress(), command.getPassword());
    if (account == null)
      throw new AuthenticationException("Sorry, we were unable to sign in with the credentials you provided.");
    log.debug("Got acccount: " + account.getApiToken());
    
    List<Long> roleIds = accountService.findRolesByAccountId(account.getAccountId());
    log.debug("Found Roles: " + roleIds.size() + ":" + command.getRememberMe());
    currentContextProvider.get().signIn(account, roleIds, command.getRememberMe());
    
    log.debug("We stateless? " + currentContextProvider.get().isStateless());    
    
    log.debug("All done");
    return new HashMap<String, Object>() {
      {
        put("destinationUrl", "/home");
        put("apiToken", account.getApiToken());
      }
    };
  }

  @POST
  @Public
  @Path("sign-out")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> signOut() {
    currentContextProvider.get().signOut();

    return new HashMap<String, Object>() {
      {
        put("destinationUrl", "/home");
      }
    };
  }
  
  @POST
  @Public
  @Path("contact")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> saveContactForm(ContactForm command) {
    accountService.saveContactForm(command);

    return new HashMap<String, Object>() {
      {
        put("destinationUrl", "/");
      }
    };
  }  
}