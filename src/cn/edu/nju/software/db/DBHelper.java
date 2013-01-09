package cn.edu.nju.software.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
	private SQLiteHelper helper;
	private SQLiteDatabase db;

	public DBHelper(Context context) {
		helper = new SQLiteHelper(context);
	}

	public void execSQL(String sql) {
		db = helper.getWritableDatabase();
		try {
			db.execSQL(sql);
		} catch (SQLException sqlEx) {
			System.out.println(sqlEx.getMessage());
		} finally {
			db.close();
		}
	}

	public Cursor rawQuery(String sql) {
		db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			return cursor;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int execSingle(String sql) throws SQLException {
		db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				return cursor.getInt(0);
			}
			return -1;
		} finally {
			cursor.close();
			db.close();
		}
	}

	public void insert(String table, ContentValues values) throws SQLException {
		SQLiteDatabase db = null;
		try {
			db = this.helper.getWritableDatabase();
			db.insert(table, null, values);
		} finally {
			db.close();
		}
	}

	public void update(String table, ContentValues values, String whereClause,
			String[] whereArgs) throws SQLException {
		SQLiteDatabase db = null;
		try {
			db = this.helper.getWritableDatabase();
			db.update(table, values, whereClause, whereArgs);
		} finally {
			db.close();
		}
	}

	public void delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = this.helper.getWritableDatabase();
			db.delete(table, whereClause, whereArgs);
		} finally {
			db.close();
		}
	}

	public void close() {
		if (db != null)
			db.close();
	}
}
