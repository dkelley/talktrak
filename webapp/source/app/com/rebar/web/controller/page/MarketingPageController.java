package com.rebar.web.controller.page;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_HTML;
import static java.lang.String.format;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rebar.web.context.CurrentContext;
import com.xmog.stack.web.Page;
import com.xmog.stack.web.annotation.Public;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/")
@Produces(CONTENT_TYPE_HTML)
public class MarketingPageController {
  @Inject
  private CurrentContext currentContext;
  
  @GET
  @Public
  public Response indexPage() {
	  if (!currentContext.isSignedIn()){	  
	    Page page = marketingPage("index", new HashMap<String, Object>() {
	      {
	        put("account", currentContext.getSignedInAccount());
	        put("title", "Welcome!");
	        put("appName", "ReBar");        
	      }
	    });
	    return Response.status(200).entity(page).build();
	  }else{
        return Response.temporaryRedirect(UriBuilder.fromPath("/home").build()).build();
	  }
  }
  


  protected Page marketingPage(final String name, Map<String, Object> model) {
    return new Page("templates/marketing.html", new HashMap<String, Object>(model) {
      {
        put("body", format("pages/marketing/%s/body.html", name));
        put("javascript", format("pages/marketing/%s/javascript.html", name));
      }
    });
  }
}