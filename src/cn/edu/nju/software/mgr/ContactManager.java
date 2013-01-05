package cn.edu.nju.software.mgr;

import java.util.List;

import android.content.Context;
import cn.edu.nju.software.dao.ContactImpl;
import cn.edu.nju.software.dao.IContact;
import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.db.DB.TABLES.CONTACT.FIELDS;
import cn.edu.nju.software.model.Contact;

public class ContactManager {
	private IContact dao;

	public ContactManager(Context context) {
		dao = new ContactImpl(context);
	}

	public void addContact(Contact contact) {
		dao.insert(contact);
	}

	public void addContact(int userId, String name, String namePinyin,
			String phone, String mobile, String email, String department,
			String nickname, String address, String note, int groupid) {
		Contact contact = new Contact(userId, name, namePinyin, phone, mobile,
				email, department, nickname, address, note, groupid);
		this.addContact(contact);
	}

	public void modifyContact(Contact contact) {
		dao.update(contact);
	}

	public void delContact(int id) {
		dao.delete(id);
	}

	public void delAllContacts() {
		dao.deleteAll();
	}

	public Contact getContactById(int id) {
		String condition = DB.TABLES.CONTACT.FIELDS.ID + " = " + id;
		List<Contact> contacts = dao.getContactsByCondition(condition);
		if (contacts.size() > 0)
			return contacts.get(0);
		else
			return null;
	}

	public List<Contact> getContactsByName(String contactName) {
		String condition = FIELDS.NAME + " like '%" + contactName + "%' ";
		return dao.getContactsByCondition(condition);
	}

	public List<Contact> getContactsByNamePinyin(String contactNamePinyin) {
		String condition = FIELDS.NAMEPINYIN + " like '" + contactNamePinyin
				+ "%' ";
		return dao.getContactsByCondition(condition);
	}

	public List<Contact> getContactsByGroupId(int groupId) {
		String condition = FIELDS.GROUPID + " = " + groupId;
		return dao.getContactsByCondition(condition);
	}

	public List<Contact> getAllContacts() {
		return dao.getContactsByCondition("1=1");
	}

	public void changeGroupByContact(int contactId, int toGroupId) {
		String sql = String.format(DB.TABLES.CONTACT.SQL.CHANGEGROUP,
				toGroupId, DB.TABLES.CONTACT.FIELDS.ID + "=" + contactId);
		dao.changeGroup(sql);
	}
}
