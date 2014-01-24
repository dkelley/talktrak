package com.rebar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.stack.Environment;
import com.xmog.stack.StackConfiguration;
import com.xmog.stack.web.server.ServerConfiguration;

/**
 * @author Dan Kelley
 */
@Singleton
public class Configuration extends StackConfiguration {
  private String signInUrl;


  @Inject
  public Configuration(Environment environment, ServerConfiguration serverConfiguration) {
    super(environment, serverConfiguration);
  }

  @Override
  protected void initialize() {
    super.initialize();
    signInUrl = getValueForProperty("signInUrl");
  }


  /**
   * @return the signInUrl
   */
  public String getSignInUrl() {
    if (signInUrl == null)
      return "/sign-in";
    return signInUrl;
  }

  /**
   * @param signInUrl the signInUrl to set
   */
  public void setSignInUrl(String signInUrl) {
    this.signInUrl = signInUrl;
  }
}
