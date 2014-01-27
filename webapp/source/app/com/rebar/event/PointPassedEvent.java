package com.rebar.event;

public class PointPassedEvent {

	private Long pointId;
	
	public PointPassedEvent(Long pointId){
		this.pointId = pointId;
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

}
