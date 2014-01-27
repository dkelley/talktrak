/**
 * 
 */
package com.rebar.service;
import static java.lang.String.format;

import static java.util.UUID.randomUUID;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.rebar.model.HighlightedPoint;
import com.rebar.model.Point;
import com.rebar.model.Role;
import com.rebar.model.Trak;
import com.rebar.model.TrakDetail;
import com.rebar.web.controller.api.AccountApiController;
import com.xmog.stack.db.Database;

/**
 * @author dkelley
 *
 */
public class TrakService {

  private Database database;
  private Log logger = LogFactory.getLog(AccountApiController.class);

  
  /**
   * 
   */
  @Inject
  public TrakService(Database database) {
    this.database = database;
  }
  
//  public List<TrakWithAccount> findUserTraks(String apiToken) {
//    logger.debug("findUserTraks");
//    return database.queryForList("select t.trak_id, t.title, t.description, t.created_date, t.updated_date, art.role_id, a.email, r.description as role, a.username, art.account_id from trak t, account_role_trak art, account a, role r where t.trak_id = art.trak_id and art.account_id = a.account_id and art.role_id = r.role_id and a.api_token = ?", TrakWithAccount.class, apiToken);
//  }  

  public List<TrakDetail> findUserTraks(Long accountId) {
    logger.debug("findUserTraks");
    return database.queryForList("select * from trak_detail where account_id = ? order by updated_date desc", TrakDetail.class, accountId);
  }  

  
//  public Trak findTrakById(Long trakId) {
//    return database.queryForObject("select * from trak t where t.trak_id = ?", Trak.class, trakId);
//  }   

//  select t.trak_id, t.title, t.description, t.created_date, t.updated_date, art.role_id, a.email, r.description as role, a.username, art.account_id, 
//  (select count(account_id) as collaborators_cnt from account_role_trak where trak_id = 1 and account_id != 1) as coll_cnt 
//from trak t, account_role_trak art, account a, role r 
//where t.trak_id = art.trak_id and art.account_id = a.account_id and art.role_id = r.role_id and t.trak_id = 1 and a.account_id = 1 
  
  public TrakDetail findTrakDetailById(Long trakId, Long accountId) {
    return database.queryForObject("select * from trak_detail where trak_id = ? and account_id = ?", TrakDetail.class, trakId, accountId);
  }    

  public TrakDetail findTrakDetailByUrl(String trakUrl) {
    return database.queryForObject("select * from trak_detail where trak_url = ?", TrakDetail.class, trakUrl);
  }   
  
  public void updateTrak(Trak trak) {
    database.executeUpdate("update trak set description = ?,title=? where trak_id = ?", trak.getDescription(), trak.getTitle(), trak.getTrakId());
  }
  
  public List<Point> findPointsForTrakAndAccountId(Long trakId, Long accountId) {
    logger.debug(format("find points for trak %d", trakId));
    return database.queryForList("select * from v_point "
      + "where trak_id = ? and created_by = ? order by sort_order asc,updated_date desc ", Point.class, trakId, accountId);
  }    

  public Long savePointForAccount(Point point, Long accountId) {
    logger.debug(format("saving points for trak %d", point.getTrakId()));
    return database.executeInsert("Insert into point(point_id, trak_id, title, sort_order, description, created_by, created_date, updated_date) VALUES (nextval('point_seq'), ?, ?,1, ?, ?, ?,?) RETURNING point_id", 
      Long.class, point.getTrakId(), point.getTitle(), point.getDescription(), accountId, new Date(), new Date());
  }  
  
  public Long saveAnonymousPoint(Point point) {
	    logger.debug(format("saving points for trak %d", point.getTrakId()));
	    return database.executeInsert("Insert into point(point_id, trak_id, title, sort_order, description, created_by, created_date, updated_date) VALUES (nextval('point_seq'), ?, ?,1, ?, ?, ?,?) RETURNING point_id", 
	      Long.class, point.getTrakId(), point.getTitle(), point.getDescription(), 0, new Date(), new Date());
	  }  
  
  public void setPointOrder(Long pointId, int sortOrder, Long accountId) {
	  logger.debug(format("updating point order"));
	    database.executeUpdate("update point set sort_order = ? from account_role_trak art where point.point_id = ? and art.account_id = ?",
	    		sortOrder, pointId, accountId);
	  }  
  
  public Long saveTrak(Trak trak) {
	    logger.debug(format("saving trak %s", trak.getDescription()));
	    return database
	      .executeInsert(
	        "INSERT INTO trak(trak_id, title, description, trak_url, created_date, updated_date) VALUES (nextval('trak_seq'), ?, ?, ?, ?, ?) RETURNING trak_id",
	        Long.class, trak.getTitle(), trak.getDescription(), randomUUID(), new Date(), new Date());    
	  }    
  
  public void saveTrakRoleForAccount(Long trakId, Long accountId, Long roleId) {
    database.executeInsertWithoutReturn("INSERT INTO account_role_trak(account_id, role_id, trak_id) VALUES (?, ?, ?);", accountId, roleId, trakId);    
  }   
  
  public List<HighlightedPoint> findHighlightedPointsForTrak(Long trakId) {
    logger.debug(format("find highlighted points for trak %d", trakId));
    return database.queryForList("select * from v_point p where p.trak_id = ? and highlighted = true and p.read_flag = false", HighlightedPoint.class, trakId);
  }   

  public void processHighlightedPoint(Long pointId, Long accountId, boolean read) {
    logger.debug(format("find highlighted points for point %d", pointId));
    database.executeUpdate("update highlighted_point set read_flag = ? where point_id =? and account_id =? ", read, pointId, accountId);
  }     

  public void retrieveHighlightedPoint(Long pointId, Long accountId) {
    logger.debug(format("remove highlighted points for point %d", pointId));
    database.executeUpdate("delete from highlighted_point where point_id =? and account_id =? ", pointId, accountId);
  }     
  
  public void highlightPoint(Long pointId, Long accountId) {
    logger.debug(format("highlighted point for trak %d", pointId));
    database.executeUpdate("insert into highlighted_point(highlighted_point_id, point_id, account_id, read_flag) values(nextval('highlighted_point_seq'), ?,?, false)", pointId, accountId);
  }     
  
  public Point findHighlightPoint(Long pointId, Long accountId) {
    logger.debug(format("find highlighted point for trak %d", pointId));
    return database.queryForObject("select * from v_point where trak_id = ? and created_by = ? and highlighted = true and read_flag = false", Point.class, pointId, accountId);
  }  
  
  
  public Point findPointById(Long pointId) {
    logger.debug(format("find point %d", pointId));
    return database.queryForObject("select * from v_point where point_id = ?", Point.class, pointId);
  }    
  
  public void deletePointById(Long pointId) {
    logger.debug(format("deleting point %d", pointId));
    database.executeUpdate("update point set active = false where point_id = ?", pointId);
  }  
  
  public void deleteTrakById(Long trakId, Long accountId) {
    logger.debug(format("deleting trak %d", trakId));
    database.executeUpdate("update trak t set active = false where t.trak_id = (select trak_id from account_role_trak where trak_id = ? and account_role_trak.account_id = ? and account_role_trak.role_id = ?)", trakId, accountId, Role.ROLE_OWNER);
  }    
}
