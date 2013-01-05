package cn.edu.nju.software.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.db.DBHelper;
import cn.edu.nju.software.model.User;

public class UserImpl implements IUser{
private DBHelper dbHelper;

public UserImpl(Context context){
	dbHelper = new DBHelper(context);
}
	@Override
	public void insert(User user) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DB.TABLES.USER.FIELDS.USERID, user.getContactId());
		values.put(DB.TABLES.USER.FIELDS.USERNAME, user.getUsername());
		values.put(DB.TABLES.USER.FIELDS.USERTYPE, user.getType());
		dbHelper.insert(DB.TABLES.USER.TABLENAME, values);

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		String sql = String.format(DB.TABLES.USER.SQL.DELETE, id);
		dbHelper.execSQL(sql);
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		String sql = DB.TABLES.USER.SQL.DROPTABLE;
		dbHelper.execSQL(sql);
		dbHelper.execSQL(DB.TABLES.USER.SQL.CREATE);
	}
	
	public int getType(String condition){
		String sql = String.format(DB.TABLES.USER.SQL.SELECT, condition);
		Cursor cursor = dbHelper.rawQuery(sql);
		int type = 0;
		while (cursor.moveToNext()) {
			type = cursor.getInt(cursor.getColumnIndex(DB.TABLES.USER.FIELDS.USERTYPE));
		}
		return type;
	}
	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DB.TABLES.USER.FIELDS.USERNAME, user.getUsername());
		values.put(DB.TABLES.USER.FIELDS.USERTYPE, user.getType());
		dbHelper.update(DB.TABLES.USER.TABLENAME, values, DB.TABLES.USER.FIELDS.USERID + "=?", new String[]{user.getId() + ""});
	}

}
