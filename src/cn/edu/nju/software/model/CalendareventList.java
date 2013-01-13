package cn.edu.nju.software.model;

import java.util.ArrayList;
import java.util.List;

public class CalendareventList {
	private int pageSize;
	private List<Calendarevent> calendarList = new ArrayList<Calendarevent>();

	public void setCalendarList(List<Calendarevent> calendarList) {
		this.calendarList = calendarList;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public List<Calendarevent> getCalendarslist() {
		return calendarList;
	}
}
