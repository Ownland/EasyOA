package cn.edu.nju.software.serviceImpl;

import java.util.List;

import android.content.Context;
import cn.edu.nju.software.dao.CalendarDAO;
import cn.edu.nju.software.dao.CalendarDAOImpl;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.CalendareventList;
import cn.edu.nju.software.service.CalendarService;
import cn.edu.nju.software.utils.NetUtil;

public class CalendarServiceImpl implements CalendarService {
	private Context context;

	public CalendarServiceImpl(Context context) {
		this.context = context;
	}
	@Override
	public void createCalendar(Calendarevent calendar) {
		// TODO Auto-generated method stub
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		calendarDAO.insert(calendar);
		//send to server
	}
	@Override
	public CalendareventList getCalendars(int ownerId,int pageIndex, int pageSize, boolean todo,
			boolean isRefresh) {
		// TODO Auto-generated method stub
		boolean networkAvailable = new NetUtil(context).goodNet();
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		CalendareventList list = new CalendareventList();
		if(networkAvailable&&isRefresh){
			//update from the server
		}
		List<Calendarevent> eList = new CalendarDAOImpl(context).getCalendarList(ownerId, todo, pageIndex, pageSize);
		list.setCalendarList(eList);
		list.setPageSize(eList.size());
		return list;
	}

}
