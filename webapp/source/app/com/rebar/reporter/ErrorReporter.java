package com.rebar.reporter;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.google.inject.Singleton;
import com.xmog.stack.reporter.StackErrorReporter;

/**
 * @author Dan Kelley
 */
@Singleton
public class ErrorReporter implements StackErrorReporter {
  private Logger logger = getLogger(getClass());
  
  public void sendErrorReport(Throwable t) {
    logger.debug("TODO: send an error report (maybe an email?) for this exception", t);
  }

  @Override
  public boolean shouldSendErrorReport(Throwable throwable) {
    // TODO Auto-generated method stub
    return false;
  }
}