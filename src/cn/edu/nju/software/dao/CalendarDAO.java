package cn.edu.nju.software.dao;

import java.util.List;

import cn.edu.nju.software.model.Calendarevent;

public interface CalendarDAO {
	public void insert(Calendarevent event);
	public List<Calendarevent> getCalendarList(int ownerId,boolean todo,int pageIndex,int pageSize);
	public void delete(int eventId);
	public void update(Calendarevent event);
}
