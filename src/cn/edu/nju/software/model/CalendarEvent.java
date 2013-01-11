package cn.edu.nju.software.model;

import java.util.Date;


public class CalendarEvent {
	public final static String EVENT_ID = "eventId";
	public final static String NAME = "name";
	public final static String BEGIN_TIME = "beginTime";
	public final static String END_TIME = "endTime";
	public final static String LOCATION = "location";
	public final static String DESCRIPTION = "description";
	public final static String REMIND = "remind";
	public final static String OWNER_ID = "ownerId";
	
	private int eventId;
	private String name;
	private Date beginTime;
	private Date endTime;
	private String location;
	private boolean remind;
	private int ownerId;
	
	
	
	public CalendarEvent() {
	}

	public CalendarEvent(int eventId, String name, Date beginTime,
			Date endTime, String location, boolean remind, 
			int ownerId) {
		super();
		this.eventId = eventId;
		this.name = name;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.location = location;
		this.remind = remind;
		this.ownerId = ownerId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isRemind() {
		return remind;
	}

	public void setRemind(boolean remind) {
		this.remind = remind;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	
	
	
}
