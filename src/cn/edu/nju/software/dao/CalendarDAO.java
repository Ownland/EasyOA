package cn.edu.nju.software.dao;

import java.util.List;
import java.util.Map;

import cn.edu.nju.software.model.Calendarevent;

public interface CalendarDAO {
	public Calendarevent insert(Calendarevent event);
	public Map<String,String> getAllCalendarByOwnerId(int ownerId);
	public List<Calendarevent> getCalendarList(int ownerId,boolean todo,int pageIndex,int pageSize);
	public Calendarevent getCalendareventByEventId(int eventId);
	public void delete(int eventId);
	public void update(Calendarevent event);
}
