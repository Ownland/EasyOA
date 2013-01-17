package cn.edu.nju.software.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.edu.nju.software.db.SQLiteHelper;
import cn.edu.nju.software.model.Calendarevent;

public class CalendarDAOImpl implements CalendarDAO {

	private SQLiteHelper dbHelper;
	public CalendarDAOImpl(Context context){
		dbHelper = new SQLiteHelper(context);
	}
	@Override
	public Calendarevent insert(Calendarevent event) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		ContentValues cv = new ContentValues();
		cv.put(Calendarevent.NAME,event.getName());
		cv.put(Calendarevent.BEGIN_TIME, sdf.format(event.getBeginTime()));
		cv.put(Calendarevent.END_TIME, sdf.format(event.getEndTime()));
		cv.put(Calendarevent.LOCATION, event.getLocation());
		cv.put(Calendarevent.DESCRIPTION,event.getDescription());
		cv.put(Calendarevent.REMIND, event.isRemind());
		cv.put(Calendarevent.OWNER_ID,event.getOwnerId());
		cv.put(Calendarevent.VERSION, event.getVersion());
		db.insert("calendar", null, cv);
		int eventId = 0;
		Cursor cursor = db.query("calendar", new String[]{"eventId"}, null, null, null, null, "eventId desc",null);
		if(!cursor.isAfterLast()){
			cursor.moveToFirst();
			eventId = cursor.getInt(cursor.getColumnIndex("eventId"));
		}
		event.setEventId(eventId);
		db.close();
		return event;
	}
	@Override
	public List<Calendarevent> getCalendarList(int ownerId,boolean todo,int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		String now = sdf.format(new Date());
		if(todo){
			cursor = db.query("calendar", new String[]{"eventId", "name","beginTime" ,"endTime","location","description","remind","ownerId","version"}, "ownerId=? and beginTime>=?", new String[]{ownerId+"",now}, null, null, "eventId desc",pageIndex*pageSize+","+pageSize);
		}else{
			cursor = db.query("calendar", new String[]{"eventId", "name","beginTime" ,"endTime","location","description","remind","ownerId","version"}, "ownerId=? and beginTime<?", new String[]{ownerId+"",now}, null, null, "eventId desc",pageIndex*pageSize+","+pageSize);
		}
		List<Calendarevent> list = new ArrayList<Calendarevent>();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			Calendarevent event = new Calendarevent();
			event.setName(cursor.getString(cursor.getColumnIndex("name")));
			try {
				event.setBeginTime(sdf.parse(cursor.getString(cursor.getColumnIndex("beginTime"))));
				event.setEndTime(sdf.parse(cursor.getString(cursor.getColumnIndex("endTime"))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.setEventId(cursor.getInt(cursor.getColumnIndex("eventId")));
			event.setDescription(cursor.getString(cursor.getColumnIndex("description")));
			event.setLocation(cursor.getString(cursor.getColumnIndex("location")));
			event.setRemind(cursor.getInt(cursor.getColumnIndex("remind"))==0?false:true);
			event.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
			event.setOwnerId(cursor.getInt(cursor.getColumnIndex("ownerId")));
			list.add(event);
		}
		db.close();
		Log.e("size",list.size()+"");
		return list;
	}
	@Override
	public void delete(int eventId) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String temp[] = new String[1];
		temp[0] = eventId+"";
		db.delete("calendar", "eventId=?",temp );
		db.close();
	}
	@Override
	public void update(Calendarevent event) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		ContentValues cv = new ContentValues();
		cv.put(Calendarevent.NAME,event.getName());
		cv.put(Calendarevent.BEGIN_TIME, sdf.format(event.getBeginTime()));
		cv.put(Calendarevent.END_TIME, sdf.format(event.getEndTime()));
		cv.put(Calendarevent.LOCATION, event.getLocation());
		cv.put(Calendarevent.DESCRIPTION,event.getDescription());
		cv.put(Calendarevent.REMIND, event.isRemind());
		cv.put(Calendarevent.OWNER_ID,event.getOwnerId());
		cv.put(Calendarevent.VERSION,event.getVersion());
		db.update("calendar",cv , "eventId=?", new String[]{event.getEventId()+""});
		db.close();
	}
	@Override
	public Map<String,String> getAllCalendarByOwnerId(int ownerId) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("calendar", new String[]{"eventId","version"}, "ownerId=?", new String[]{ownerId+""}, null, null, null,null);
		Map<String,String> version_map  = new HashMap<String,String>();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			version_map.put(cursor.getInt(cursor.getColumnIndex("eventId"))+"",cursor.getInt(cursor.getColumnIndex("version"))+"" );
		}
		db.close();
		return version_map;
	}
	@Override
	public Calendarevent getCalendareventByEventId(int eventId) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("calendar", new String[]{"eventId", "name","beginTime" ,"endTime","location","description","remind","ownerId","version"}, "eventId=?", new String[]{eventId+""}, null, null, null,null);
		Calendarevent event = null;
		if(!cursor.isAfterLast()){
			cursor.moveToFirst();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
			event = new Calendarevent();
			event.setName(cursor.getString(cursor.getColumnIndex("name")));
			try {
				event.setBeginTime(sdf.parse(cursor.getString(cursor.getColumnIndex("beginTime"))));
				event.setEndTime(sdf.parse(cursor.getString(cursor.getColumnIndex("endTime"))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.setEventId(cursor.getInt(cursor.getColumnIndex("eventId")));
			event.setDescription(cursor.getString(cursor.getColumnIndex("description")));
			event.setLocation(cursor.getString(cursor.getColumnIndex("location")));
			event.setRemind(cursor.getInt(cursor.getColumnIndex("remind"))==0?false:true);
			event.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
			event.setOwnerId(cursor.getInt(cursor.getColumnIndex("ownerId")));
		}
		return event;
	}

}
