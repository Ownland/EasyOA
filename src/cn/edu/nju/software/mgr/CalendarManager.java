package cn.edu.nju.software.mgr;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import cn.edu.nju.software.dao.CalendarDAO;
import cn.edu.nju.software.dao.CalendarDAOImpl;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.CalendareventList;
import cn.edu.nju.software.service.ICalendareventService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.utils.NetUtil;

public class CalendarManager{
	private Context context;
	private static final String calanderURL = "content://com.android.calendar/calendars";  
	private static final String calanderEventURL = "content://com.android.calendar/events";  
	private static final String calanderRemiderURL = "content://com.android.calendar/reminders";  
	public CalendarManager(Context context) {
		this.context = context;
	}
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
		Calendarevent calendarevent = calendarDAO.insert(calendar);
		
		//send to server
	}
	public CalendareventList getCalendars(int ownerId,int pageIndex, int pageSize, boolean todo,
			boolean isRefresh) {
		// TODO Auto-generated method stub
		boolean networkAvailable = new NetUtil(context).goodNet();
		CalendareventList list = new CalendareventList();
		if(networkAvailable&&isRefresh){
			//update from the server
			Map<String,String> version_map = new CalendarDAOImpl(context).getAllCalendarByOwnerId(ownerId);
			System.out.println(version_map.size());
			ICalendareventService calendarService = ClientServiceHelper.getCalendareventService();
			List<Calendarevent> toBeUpdatedList = calendarService.getCalendareventList(version_map, ownerId);
			System.out.println(toBeUpdatedList.size());
			CalendarDAO calendarDAO = new CalendarDAOImpl(context);
			for(Calendarevent event:toBeUpdatedList){
				if(calendarDAO.getCalendareventByEventId(event.getEventId())!=null)
					calendarDAO.update(event);
				else
					calendarDAO.insert(event);
			}
		}
		List<Calendarevent> eList = new CalendarDAOImpl(context).getCalendarList(ownerId, todo, pageIndex, pageSize);
		list.setCalendarList(eList);
		list.setPageSize(eList.size());
		return list;
	}
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
		calendar.setVersion(calendar.getVersion()+1);
		CalendarDAO calendarDAO = new CalendarDAOImpl(context);
		calendarDAO.update(calendar);
		//send to server
	}

}
