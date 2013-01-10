package cn.edu.nju.software.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.db.DBHelper;
import cn.edu.nju.software.db.SQLiteHelper;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;

public class EmailDAOImpl implements EmailDAO{
	private SQLiteHelper dbHelper;
	public EmailDAOImpl(Context context){
		dbHelper = new SQLiteHelper(context);
	}
	public void insert(Email email){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		ContentValues cv = new ContentValues();
		cv.put("number",email.getId());
		cv.put("title", email.getTitle());
		cv.put("sender", email.getSender());
		cv.put("reciever", email.getReciever());
		cv.put("content", email.getContent());
		cv.put("date", sdf.format(email.getDate()));
		cv.put("type", email.getType().value());
		db.insert("email", null, cv);
		db.close();
	}
	public void delete(Email email){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String temp[] = new String[2];
		temp[0] = email.getId()+"";
		temp[1] = email.getType().value()+"";
		db.delete("email", "number=? and type=?",temp );
		db.close();
	}
	
	public Email findEmailByNumber(int number,EmailType type){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("email", new String[]{"number,title,sender,reciever,content,date,type"}, "number=? and type=?", new String[]{number+"",type.value()+""}, null, null, null);
		Email email = new Email();
		cursor.moveToFirst();
		if(cursor.isAfterLast()){
			cursor.close();
			db.close();
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		email.setId(cursor.getInt(cursor.getColumnIndex("number")));
		email.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		email.setSender(cursor.getString(cursor.getColumnIndex("sender")));
		email.setReciever(cursor.getString(cursor.getColumnIndex("reciever")));
		email.setContent(cursor.getString(cursor.getColumnIndex("content")));
		email.setType(EmailType.valueOf(cursor.getInt(cursor.getColumnIndex("type"))));
		try {
			email.setDate(sdf.parse(cursor.getString(cursor.getColumnIndex("date"))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cursor.close();
		db.close();
		return email;
	}
	public void update(Email email,int number,EmailType type){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("number",email.getId());
		cv.put("title", email.getTitle());
		cv.put("sender", email.getSender());
		cv.put("reciever", email.getReciever());
		cv.put("content", email.getContent());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		cv.put("date", sdf.format(email.getDate()));
		cv.put("type", email.getType().value());
		db.update("email",cv , "number=? and type=?", new String[]{number+"",type.value()+""});
		db.close();
	}
	public int findMaxNumber(EmailType type){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("email", new String[]{"number"}, "type=?", new String[]{type.value()+""}, null, null, "number desc");
		cursor.moveToFirst();
		if(cursor.isAfterLast()){
			cursor.close();
			db.close();
			return 0;
		}else{
			int result =  cursor.getInt(cursor.getColumnIndex("number"));
			cursor.close();
			db.close();
			return result;
		}
	}
	public int findMinNumber(EmailType type){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("email", new String[]{"number"}, "type=?", new String[]{type.value()+""}, null, null, "number asc");
		cursor.moveToFirst();
		if(cursor.isAfterLast()){
			cursor.close();
			db.close();
			return 0;
		}else{
			int result =  cursor.getInt(cursor.getColumnIndex("number"));
			cursor.close();
			db.close();
			return result;
		}
	}
}
