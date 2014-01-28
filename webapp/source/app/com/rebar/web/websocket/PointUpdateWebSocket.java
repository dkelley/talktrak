package com.rebar.web.websocket;


import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.rebar.event.PointPassedEvent;
import com.rebar.model.Point;
import com.rebar.service.TrakService;
import com.xmog.stack.templating.json.JsonMapper;
import com.xmog.stack.web.websocket.StackWebSocket;

/**
 * @author Dan Kelley
 */
@Path("/websocket/point/update")
public class PointUpdateWebSocket extends StackWebSocket {
  private Long trakId;

  @Inject
  private TrakService trakService;

  @Inject
  private JsonMapper jsonMapper;
  
  private EventBus eventBus;

  private Logger logger = getLogger(getClass());

  @Inject
  public PointUpdateWebSocket(EventBus eventBus) {
    super();
    eventBus.register(this);
    this.eventBus = eventBus;
    logger.debug(format("Register PointUpdateWebSocket with %s", eventBus));
  }
  
  @Override
  protected void onClose(int statusCode, String reason) {
    getEventBus().unregister(this);
  }

  @Subscribe
  public void handlePointPassedEvent(PointPassedEvent event) {

    logger.debug(format("%s received %s. a point passed...", this, event));

    Point point = trakService.findPointById(event.getPointId());
    
    getRemote().sendStringByFuture(jsonMapper.toJson(point));
  }

  @Override
  protected void onMessage(String json) {
	  logger.debug("onMessage /websocket/location/update");
	  // nothing coming down now
  }
  
  public EventBus getEventBus() {
    return eventBus;
  }
}