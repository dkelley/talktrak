package com.rebar.web.controller.api;

import static com.rebar.model.Role.ROLE_OWNER;
import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rebar.command.SortedPoints;
import com.rebar.model.Point;
import com.rebar.model.Role;
import com.rebar.model.Trak;
import com.rebar.model.TrakDetail;
import com.rebar.service.TrakService;
import com.rebar.web.context.CurrentContext;
import com.xmog.stack.web.annotation.RoleRequired;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/api/trak")
@Produces(CONTENT_TYPE_JSON)
@SuppressWarnings("serial")
public class TrakApiController {
  @Inject
  private CurrentContext currentContext;
  
  @Inject
  private TrakService trakService;
  private Logger logger = getLogger(getClass());
 
  @GET
  public Map<String, Object> traks() {
    return new HashMap<String, Object>() {
      {
        put("traks", trakService.findUserTraks(currentContext.getSignedInAccountId()));
      }
    };    
  }  
  
  @POST
  @Consumes(CONTENT_TYPE_JSON)  
  public Map<String, Object> createTrak(Trak command) {    
    final Long trakId = trakService.saveTrak(command);
    if (trakId != null)
      trakService.saveTrakRoleForAccount(trakId, currentContext.getSignedInAccountId(), Role.ROLE_OWNER);    
    return new HashMap<String, Object>() {
      {
        put("success","true");
        put("trakId",trakId);
      }
    };    
  }     
  
  @DELETE
  @Path("/{id}")
  @RoleRequired(ROLE_OWNER)
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> deleteTrak(@PathParam("id") final Long id) {    
    trakService.deleteTrakById(id, currentContext.getSignedInAccountId());
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }  
  
  @GET 
  @Path("{id}")
  public TrakDetail getTrak(@PathParam("id") Long id) {
    return trakService.findTrakDetailById(id, currentContext.getSignedInAccountId());    
  }    
 
  @PUT 
  @Path("{id}")
  public Trak saveTrak(@PathParam("id") Long id, Trak command) {
    logger.debug(format("Updating %d %s)",id, command.getTitle()));
    command.setTrakId(id);
    trakService.updateTrak(command);
    return trakService.findTrakDetailById(id, currentContext.getSignedInAccountId());
  }   
  
  @GET
  @Path("/{id}/points")  
  public List<Point> points(@PathParam("id") final Long id) {    
    return trakService.findPointsForTrakAndAccountId(id, currentContext.getSignedInAccountId());
  }    

  @PUT
  @Path("/{id}/sortpoints")  
  @Consumes(CONTENT_TYPE_JSON)
  public void sortPoints(@PathParam("id") final Long id, SortedPoints sortedPoints) {
	  logger.debug("Points" + sortedPoints.getPoints().size());
	  int x = 1;
	  for (Long pointId : sortedPoints.getPoints()){
		  if (pointId != null){
			trakService.setPointOrder(pointId, x, currentContext.getSignedInAccountId());
		  	x++;
		  }
	  }
  }      
  
  @GET
  @Path("/{id}/highlighted")
  public Map<String, Object> highlightedPoints(@PathParam("id") final Long id) {
    return new HashMap<String, Object>() {
      {
        put("points", trakService.findHighlightedPointsForTrak(id));
      }
    };    
  }    
}