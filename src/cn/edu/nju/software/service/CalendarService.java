package cn.edu.nju.software.service;

import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.CalendareventList;

public interface CalendarService {
	public void createCalendar(Calendarevent calendar);
	public CalendareventList getCalendars(int ownerId,int pageIndex,int pageSize,boolean todo,boolean isRefresh);
}
