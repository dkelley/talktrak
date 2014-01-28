package com.rebar.event;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public class PointPassedEvent {

	private Long pointId;
	private static final Logger logger = getLogger(PointPassedEvent.class);
		
	public PointPassedEvent(Long pointId){
		this.pointId = pointId;
		logger.debug(format("Created event %d", pointId));
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

}
