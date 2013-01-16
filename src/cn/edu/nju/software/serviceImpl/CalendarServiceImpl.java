package cn.edu.nju.software.serviceImpl;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import cn.edu.nju.software.dao.CalendarDAO;
import cn.edu.nju.software.dao.CalendarDAOImpl;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.CalendareventList;
import cn.edu.nju.software.service.CalendarService;
import cn.edu.nju.software.utils.NetUtil;

public class CalendarServiceImpl implements CalendarService {
	private Context context;
	private static final String calanderURL = "content://com.android.calendar/calendars";  
	private static final String calanderEventURL = "content://com.android.calendar/events";  
	private static final String calanderRemiderURL = "content://com.android.calendar/reminders";  
	public CalendarServiceImpl(Context context) {
		this.context = context;
	}
	@Override
	public void createCalendar(Calendarevent calendar) {
		// TODO Auto-generated method stub
		if(calendar.isRemind()){
			String calId = "";  
	        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null,   
	                null, null, null);  
	        if(userCursor.getCount() > 0){  
	            userCursor.moveToFirst();  
	            calId = userCursor.getString(userCursor.getColumnIndex("_id"));  
	        }  
	        ContentValues event = new ContentValues();  
	        event.put("title", calendar.getName());  
	        event.put("description", calendar.getDescription());  
	        event.put("calendar_id",calId);  
	        event.put("dtstart", calendar.getBeginTime().getTime()); 
	        event.put("dtend", calendar.getEndTime().getTime());  
	        event.put("hasAlarm",1);  
	          
	        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);  
	        long id = Long.parseLong( newEvent.getLastPathSegment() );  
	        ContentValues values = new ContentValues();  
	        values.put( "event_id", id );  
	        values.put( "minutes", 0);
	        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);  
		}
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		calendarDAO.insert(calendar);
		//send to server
	}
	@Override
	public CalendareventList getCalendars(int ownerId,int pageIndex, int pageSize, boolean todo,
			boolean isRefresh) {
		// TODO Auto-generated method stub
		boolean networkAvailable = new NetUtil(context).goodNet();
		CalendareventList list = new CalendareventList();
		if(networkAvailable&&isRefresh){
			//update from the server
			//new CalendarDAOImpl(context)
		}
		List<Calendarevent> eList = new CalendarDAOImpl(context).getCalendarList(ownerId, todo, pageIndex, pageSize);
		list.setCalendarList(eList);
		list.setPageSize(eList.size());
		return list;
	}
	@Override
	public void deleteCalendar(int eventId) {
		// TODO Auto-generated method stub
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		boolean networkAvailable = new NetUtil(context).goodNet();
		if(networkAvailable){
			//delete from server
			calendarDAO.delete(eventId);
		}
	}
	public void updateCalendar(Calendarevent calendar) {
		// TODO Auto-generated method stub
		if(calendar.isRemind()){
			String calId = "";  
	        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null,   
	                null, null, null);  
	        if(userCursor.getCount() > 0){  
	            userCursor.moveToFirst();  
	            calId = userCursor.getString(userCursor.getColumnIndex("_id"));  
	              
	        }  
	        ContentValues event = new ContentValues();  
	        event.put("title", calendar.getName());  
	        event.put("description", calendar.getDescription());  
	        event.put("calendar_id",calId);  
	        event.put("dtstart", calendar.getBeginTime().getTime()); 
	        event.put("dtend", calendar.getEndTime().getTime());  
	        event.put("hasAlarm",1);  
	          
	        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);  
	        long id = Long.parseLong( newEvent.getLastPathSegment() );  
	        ContentValues values = new ContentValues();  
	        values.put( "event_id", id );  
	        values.put( "minutes", 0);
	        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);  
		}
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		calendarDAO.update(calendar);
		//send to server
	}

}
