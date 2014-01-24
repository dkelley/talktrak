/**
 * 
 */
package com.rebar.service;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.rebar.model.Challenge;
import com.xmog.stack.db.Database;

/**
 * @author dkelley
 *
 */
public class ChallengeService {

  @SuppressWarnings("unused")
  private Database database;
  
  /**
   * 
   */
  @Inject
  public ChallengeService(Database database) {
    this.database = database;
  }
  
  public List<Challenge> findChallegesForTrak(Long trakId) {
    // TODO: pull from database instead of just faking it
    Challenge challenge = new Challenge();
    challenge.setChallengeId(1l);
    challenge.setDescription("This is a fake challenge");
    challenge.setTitle("First Challenge");
    challenge.setChallengeId(1l);
    
    List<Challenge> list = new ArrayList<Challenge>();
    list.add(challenge);
    return list;
  }  
  
}
