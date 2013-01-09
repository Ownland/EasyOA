package cn.edu.nju.software.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public final static String DATABASE_Name = "EasyOA.db";
    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    	db.execSQL("create table if not exists email (id integer primary key autoincrement,number integer,title varchar(50),sender varchar(50),reciever varchar(50),content varchar(255),date datetime,type integer)");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public void deleteDatabase(Context context){
		context.deleteDatabase("EasyOA.db");
	}
 
}

