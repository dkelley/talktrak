package com.rebar.reporter;

import javax.servlet.http.HttpServletRequest;

import com.xmog.stack.db.QueryLogRecord;
import com.xmog.stack.reporter.AbstractSlowPerformanceReporter;

public class SlowPerformanceReporter extends AbstractSlowPerformanceReporter {

  @Override
  public void sendSlowQueryReport(QueryLogRecord queryLogRecord) {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendSlowHttpRequestReport(HttpServletRequest httpRequest, long httpRequestDurationInMilliseconds) {
    // TODO Auto-generated method stub

  }

}
