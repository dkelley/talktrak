package com.rebar.web.controller.api;

import com.xmog.stack.exception.ValidationException;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rebar.model.Point;
import com.rebar.service.TrakService;
import com.rebar.web.context.CurrentContext;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/api/point")
@Produces(CONTENT_TYPE_JSON)
@SuppressWarnings("serial")
public class PointsApiController {
  @Inject
  private CurrentContext currentContext;
  
  @Inject
  private TrakService trakService;

  @GET
  @Path("/{id}")
  public Point getPoint(@PathParam("id") final Long id) {
    return trakService.findPointById(id);
  }

  @DELETE
  @Path("/{id}")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> deletePoint(@PathParam("id") final Long id) {
    trakService.deletePointById(id);
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }

  @POST
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> createPoint(Point command) {    
    trakService.savePointForAccount(command, currentContext.getSignedInAccountId());
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }
  
  @POST
  @Path("/{id}/processed")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> processHighlightedPoints(@PathParam("id") final Long pointId) {
    trakService.processHighlightedPoint(pointId, currentContext.getSignedInAccountId(), true);
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }
  
  @POST
  @Path("/{id}/retrieve")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> retrievePoint(@PathParam("id") final Long pointId) {
    trakService.retrieveHighlightedPoint(pointId, currentContext.getSignedInAccountId());
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }    
  
  @POST
  @Path("/{id}/highlight")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> highlightPoint(@PathParam("id") final Long pointId) {
    // check if you already highlighted
    Point point = trakService.findHighlightPoint(pointId, currentContext.getSignedInAccountId());
   
    if (point!=null){
      if (point.isHighlighted()){
        throw new ValidationException("point is already highlighted");
      }else{
          trakService.processHighlightedPoint(pointId, currentContext.getSignedInAccountId(), false);
      }
    }else{
        trakService.highlightPoint(pointId, currentContext.getSignedInAccountId());      
    }
    return new HashMap<String, Object>() {
      {
        put("success", "true");
      }
    };
  }  
}
