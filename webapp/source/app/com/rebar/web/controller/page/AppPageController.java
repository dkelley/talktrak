package com.rebar.web.controller.page;

import static com.rebar.model.Role.ROLE_EDITOR;
import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_HTML;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rebar.web.context.CurrentContext;
import com.rebar.web.controller.api.AccountApiController;
import com.rebar.model.Role;
import com.rebar.model.TrakDetail;
import com.rebar.service.TrakService;
import com.xmog.stack.web.Page;
import com.xmog.stack.web.annotation.Public;
import com.xmog.stack.web.annotation.RoleRequired;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/home")
@SuppressWarnings("serial")
@Produces(CONTENT_TYPE_HTML)
public class AppPageController {
  @Inject
  private TrakService trakService;

  @Inject
  private CurrentContext currentContext;

  private Log logger = LogFactory.getLog(AccountApiController.class);

  @GET
  @Public
  @Path("/sign-in")
  public Page signinPage() {
    return appPage("signin", new HashMap<String, Object>() {
      {
        put("title", "Sign In");
        put("appName", "ReBar");
      }
    });
  }
  
  @GET
  @Public
  @Path("/sign-up")
  public Page signUpPage() {
    return appPage("signup", new HashMap<String, Object>() {
      {
        put("title", "Sign Up");
        put("appName", "ReBar");
      }
    });
  }  
  
  @GET
  @RoleRequired(ROLE_EDITOR)
  public Page indexPage() {
    final List<TrakDetail> traks = trakService.findUserTraks(currentContext.getSignedInAccountId());
    return appPage("index", new HashMap<String, Object>() {
      {
        put("traks", traks);
        put("appName", "ReBar");
      }
    });
  }
  
  @GET
  @Path("/trak")
  public Page newTrak() {
    final TrakDetail trak = new TrakDetail();
    trak.setTitle("New Trak");
    trak.setDescription("Add your trak content");
    final Long trakId = trakService.saveTrak(trak);
    trak.setTrakId(trakId);
    if (trakId != null)
      trakService.saveTrakRoleForAccount(trakId, currentContext.getSignedInAccountId(), Role.ROLE_OWNER);    
    return appPage("trak", new HashMap<String, Object>() {
      {
        put("isEditable", true);
    	put("action", "edit");        
        put("title", "Create Trak");
        put("trak", trak);
        put("appName", "ReBar");
      }
    });
  }    
    
  
  @GET
  @Path("/trak/{trakId}/{action}")
  public Page editTrak(@PathParam("trakId") Long trakId, @PathParam("action") final String trakAction) {
    final TrakDetail trak = trakService.findTrakDetailById(trakId, currentContext.getSignedInAccountId());
    final boolean isEditable = currentContext.getSignedInAccount().getRoleForTrak(trakId)== Role.ROLE_OWNER;
    return appPage("trak", new HashMap<String, Object>() {
      {
        put("isEditable", isEditable);
    	put("action", trakAction);
        put("trak", trak);
        put("appName", "ReBar");
      }
    });
  }
  
  @GET
  @Public
  @Path("/{trakUrl}")
  public Page loadTrakForCollab(@PathParam("trakUrl") String trakUrl) {
    final TrakDetail trak = trakService.findTrakDetailByUrl(trakUrl);
    return appPage("collab", new HashMap<String, Object>() {
      {
        put("trak", trak);
        put("appName", "ReBar");
      }
    });
  }

  protected Page appPage(final String name, Map<String, Object> model) {
    return new Page("templates/app.html", new HashMap<String, Object>(model) {
      {
        put("body", format("pages/app/%s/body.html", name));
        put("ROLE_EDITOR", Role.ROLE_EDITOR);
        put("ROLE_ASSISTANT", Role.ROLE_ASSISTANT);
        put("ROLE_OWNER", Role.ROLE_OWNER);
        put("javascript", format("pages/app/%s/javascript.html", name));
      }
    });
  }
}