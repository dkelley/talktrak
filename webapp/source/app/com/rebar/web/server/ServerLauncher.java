package com.rebar.web.server;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.xmog.stack.StackConfiguration;
import com.xmog.stack.reporter.StackErrorReporter;
import com.xmog.stack.reporter.StackSlowPerformanceReporter;
import com.xmog.stack.service.StackAccountService;
import com.xmog.stack.web.StackCommonPageModelProvider;
import com.xmog.stack.web.context.StackAccount;
import com.xmog.stack.web.context.StackCurrentContext;
import com.xmog.stack.web.interceptor.AuthenticationInterceptor;
import com.xmog.stack.web.provider.StackExceptionMapper;
import com.xmog.stack.web.server.StackServerConfigurer;
import com.xmog.stack.web.server.StackServerLauncher;
import com.xmog.stack.web.server.StackServletModule;
import com.rebar.Configuration;
import com.rebar.model.Account;
import com.rebar.reporter.ErrorReporter;
import com.rebar.reporter.SlowPerformanceReporter;
import com.rebar.service.AccountService;
import com.rebar.web.CommonPageModelProvider;
import com.rebar.web.context.CurrentContext;
import com.rebar.web.exception.ExceptionMapper;

/**
 * @author Transmogrify LLC.
 */
public class ServerLauncher extends StackServerLauncher {
  public static void main(String[] args) throws Exception {
    new ServerLauncher().launchServer();
  }

  @Override
  protected StackServerConfigurer createStackServerConfigurer() {
    return new StackServerConfigurer() {
      @Override
      public StackServletModule<Long, Long> createStackServletModule() {
        return new StackServletModule<Long, Long>(this) {
          @Override
          protected String getControllerPackageName() {
            return "com.rebar.web.controller";
          }
          
          @Override
          protected String getWebSocketPackageName() {
            return "com.rebar.web.websocket";
          }          

          @Override
          protected Class<? extends StackExceptionMapper> getStackExceptionMapperClass() {
            return ExceptionMapper.class;
          }

          @Override
          protected Class<? extends StackConfiguration> getStackConfigurationClass() {
            return Configuration.class;
          }

          @Override
          protected Class<? extends StackCommonPageModelProvider> getStackCommonPageModelProviderClass() {
            return CommonPageModelProvider.class;
          }

          @Override
          protected Class<? extends StackErrorReporter> getStackErrorReporterClass() {
            return ErrorReporter.class;
          }

          @Override
          protected Class<? extends StackCurrentContext<? extends StackAccount<Long>, Long, Long>> getStackCurrentContextClass() {
            return CurrentContext.class;
          }

          @Override
          protected void bindToStackCurrentContextClass() {
            bind(new TypeLiteral<StackCurrentContext<Account, Long, Long>>() {}).to(CurrentContext.class);
          }

          @Override
          protected Class<? extends StackAccountService<? extends StackAccount<Long>, Long, Long>> getStackAccountServiceClass() {
            return AccountService.class;
          }          
          
          @Override
          protected void bindToStackAccountServiceClass() {
            bind(new TypeLiteral<StackAccountService<Account, Long, Long>>() {}).to(AccountService.class);
          }
          
          @Override
          protected AuthenticationInterceptor<Long, Long> createAuthenticationInterceptor() {
            return new AuthenticationInterceptor<Long, Long>();
          }          

          @Override
          protected Class<? extends StackSlowPerformanceReporter> getStackSlowPerformanceReporterClass() {
            return SlowPerformanceReporter.class;
          }

          @Override
          public void onStartup(Injector injector) {
            super.onStartup(injector);
            // TODO: add any logic that must be executed on startup
          }

          @Override
          public void onTeardown(Injector injector) {
            super.onTeardown(injector);
            // TODO: add any logic that must be executed on shutdown (close thread pools, etc.)
          }
        };
      }
      
      @Override
      public Module createStackOverridesModule() {
        return new AbstractModule() {
          @Override
          protected void configure() {}

          // TODO: if needed, override anything in the stack servlet module.
          // For example, you might add a @Provider method that returns a MailgunMessageSender
          // which would replace the Stack's default print-to-console FakeEmailMessageSender.
        };
      }
    };
  }
}