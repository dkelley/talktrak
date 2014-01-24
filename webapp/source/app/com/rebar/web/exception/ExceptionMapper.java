package com.rebar.web.exception;

import static com.xmog.stack.util.FormattingUtils.urlWithParameters;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.rebar.Configuration;
import com.rebar.reporter.ErrorReporter;
import com.xmog.stack.web.Page;
import com.xmog.stack.web.RequestType;
import com.xmog.stack.web.RequestTypeManager;
import com.xmog.stack.web.provider.StackExceptionMapper;
/**
 * @author Dan Kelley
 */
@Singleton
public class ExceptionMapper extends StackExceptionMapper {
  @Inject
  Configuration configuration;
  
  @Inject
  public ExceptionMapper(Provider<HttpServletRequest> httpRequestProvider, RequestTypeManager requestTypeManager,
      ErrorReporter errorReporter, Configuration configuration) {
    super(httpRequestProvider, requestTypeManager, errorReporter, configuration);
  }

  @Override
  protected Page getErrorPage(HttpServletRequest request, RequestType requestType, Throwable throwable,
      Map<String, Object> model) {
    return new Page("templates/standard.html", new HashMap<String, Object>(model) {
      {
        put("title", "Sorry, an error occurred.");
        put("body", "pages/marketing/error/body.html");
      }
    });
  }

  @Override
  protected String getSignInPageUrl(HttpServletRequest request, RequestType requestType, Throwable throwable) {
    return format("%s?destinationUrl=%s", configuration.getSignInUrl(), urlWithParameters(request));
  }
}