package com.rebar.web.context;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rebar.model.Account;
import com.xmog.stack.web.RememberMeManager;
import com.xmog.stack.web.RequestTypeManager;
import com.xmog.stack.web.context.AbstractCurrentContext;

/**
 * @author Dan Kelley
 */
public class CurrentContext extends AbstractCurrentContext<Account> {
  @Inject
  public CurrentContext(Provider<HttpServletRequest> httpRequestProvider, RequestTypeManager requestTypeManager,
      RememberMeManager rememberMeManager) {
    super(httpRequestProvider, requestTypeManager, rememberMeManager);
  }
}