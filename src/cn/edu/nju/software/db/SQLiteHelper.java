package cn.edu.nju.software.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context) {
		super(context, DB.DATABASENAME, null, DB.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DB.TABLES.CONTACT.SQL.CREATE);
		db.execSQL(DB.TABLES.GROUP.SQL.CREATE);
    	db.execSQL("create table if not exists email (id integer primary key autoincrement,number integer,title varchar(50),sender varchar(50),reciever varchar(50),content varchar(255),date datetime,type integer)");
    	db.execSQL("create table if not exists calendar (eventId integer primary key autoincrement,name varchar(50),beginTime datetime,endTime datetime,location varchar(50),description varchar(50),remind boolean,ownerId integer,version integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DB.TABLES.CONTACT.SQL.DROPTABLE);
		db.execSQL(DB.TABLES.GROUP.SQL.DROPTABLE);
		db.execSQL("drop table if exists email");
		db.execSQL("drop table if exists calendar");
		onCreate(db);
	}

}
