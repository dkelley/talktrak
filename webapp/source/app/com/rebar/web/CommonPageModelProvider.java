package com.rebar.web;

import java.util.HashMap;
import java.util.Map;

import com.rebar.web.context.CurrentContext;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.xmog.stack.web.StackCommonPageModelProvider;

/**
 * @author Dan Kelley
 */
@Singleton
public class CommonPageModelProvider implements StackCommonPageModelProvider {
  private Provider<CurrentContext> currentContextProvider;

  @Inject
  public CommonPageModelProvider(Provider<CurrentContext> currentContextProvider) {
    this.currentContextProvider = currentContextProvider;
  }

  public Map<String, Object> getCommonPageModel() {
    return new HashMap<String, Object>() {
      {
        put("currentContext", currentContextProvider.get());
        // TODO: add any other data you'd like to be exposed to page Velocity templates
      }
    };
  }
}