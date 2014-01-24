package com.rebar.web.controller.api;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rebar.model.Challenge;
import com.rebar.service.ChallengeService;
import com.xmog.stack.web.annotation.Public;

/**
 * @author Dan Kelley
 */
@Singleton
@Path("/api/challenges")
@Produces(CONTENT_TYPE_JSON)
public class ChallengeApiController {  
  @Inject
  private ChallengeService challengeService;
    
  @GET
  @Public
  @Path("list")
  public List<Challenge> challenges() {
    return challengeService.findChallegesForTrak(1l);
  }  
}