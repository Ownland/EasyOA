package cn.edu.nju.software.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.db.DBHelper;
import cn.edu.nju.software.model.Contact;

public class ContactImpl implements IContact {
	private DBHelper dbHelper;

	public ContactImpl(Context context) {
		dbHelper = new DBHelper(context);
	}

	@Override
	public void insert(Contact contact) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DB.TABLES.CONTACT.FIELDS.ID, contact.getId());
		values.put(DB.TABLES.CONTACT.FIELDS.NAME, contact.getName());
		values.put(DB.TABLES.CONTACT.FIELDS.NAMEPINYIN, contact.getNamePinyin());
		values.put(DB.TABLES.CONTACT.FIELDS.PHONE, contact.getPhone());
		values.put(DB.TABLES.CONTACT.FIELDS.MOBILE, contact.getMobile());
		values.put(DB.TABLES.CONTACT.FIELDS.EMAIL, contact.getEmail());
		values.put(DB.TABLES.CONTACT.FIELDS.DEPARTMENT, contact.getDepartment());
		values.put(DB.TABLES.CONTACT.FIELDS.NICKNAME, contact.getNickname());
		values.put(DB.TABLES.CONTACT.FIELDS.ADDRESS, contact.getAddress());
		values.put(DB.TABLES.CONTACT.FIELDS.NOTE, contact.getNote());
		values.put(DB.TABLES.CONTACT.FIELDS.GROUPID, 0);
		dbHelper.insert(DB.TABLES.CONTACT.TABLENAME, values);

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		String sql = String.format(DB.TABLES.CONTACT.SQL.DELETE, id);
		dbHelper.execSQL(sql);
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		String sql = DB.TABLES.CONTACT.SQL.DROPTABLE;
		dbHelper.execSQL(sql);
		dbHelper.execSQL(DB.TABLES.CONTACT.SQL.CREATE);
	}

	@Override
	public void update(Contact contact) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DB.TABLES.CONTACT.FIELDS.NAME, contact.getName());
		values.put(DB.TABLES.CONTACT.FIELDS.NAMEPINYIN, contact.getNamePinyin());
		values.put(DB.TABLES.CONTACT.FIELDS.PHONE, contact.getPhone());
		values.put(DB.TABLES.CONTACT.FIELDS.MOBILE, contact.getMobile());
		values.put(DB.TABLES.CONTACT.FIELDS.EMAIL, contact.getEmail());
		values.put(DB.TABLES.CONTACT.FIELDS.DEPARTMENT, contact.getDepartment());
		values.put(DB.TABLES.CONTACT.FIELDS.NICKNAME, contact.getNickname());
		values.put(DB.TABLES.CONTACT.FIELDS.ADDRESS, contact.getAddress());
		values.put(DB.TABLES.CONTACT.FIELDS.NOTE, contact.getNote());
		dbHelper.update(DB.TABLES.CONTACT.TABLENAME, values,
				DB.TABLES.CONTACT.FIELDS.ID + "= ? ",
				new String[] { contact.getId() + "" });
	}

	@Override
	public List<Contact> getContactsByCondition(String condition) {
		// TODO Auto-generated method stub
		String sql = String.format(DB.TABLES.CONTACT.SQL.SELECT, condition);
		Cursor cursor = dbHelper.rawQuery(sql);

		List<Contact> contacts = new ArrayList<Contact>();
		while (cursor.moveToNext()) {
			Contact contact = new Contact();
			contact.setId(cursor.getInt(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.ID)));
			contact.setName(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.NAME)));
			contact.setNamePinyin(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.NAMEPINYIN)));
			contact.setPhone(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.PHONE)));
			contact.setMobile(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.MOBILE)));
			contact.setEmail(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.EMAIL)));
			contact.setDepartment(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.DEPARTMENT)));
			contact.setNickname(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.NICKNAME)));
			contact.setAddress(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.ADDRESS)));
			contact.setNote(cursor.getString(cursor
					.getColumnIndex(DB.TABLES.CONTACT.FIELDS.NOTE)));
			contacts.add(contact);
		}
		dbHelper.close();
		return contacts;
	}

	@Override
	public void changeGroup(String sql) {
		// TODO Auto-generated method stub
		dbHelper.execSingle(sql);
	}

}
