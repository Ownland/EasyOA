package cn.edu.nju.software.mgr;

import java.util.List;

import android.content.Context;
import cn.edu.nju.software.dao.ContactImpl;
import cn.edu.nju.software.dao.IContact;
import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.db.DB.TABLES.CONTACT.FIELDS;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.utils.DESUtil;

public class ContactManager {
	private IContact dao;
	private DESUtil desUtil;
	public ContactManager(Context context, String key) {
		dao = new ContactImpl(context);
		desUtil = new DESUtil(key);
	}

	public void addContact(Contact contact) {
		encryptContact(contact);
		dao.insert(contact);
	}

	public void addContact(int userId, String name, String namePinyin,
			String phone, String mobile, String email, String department,
			String nickname, String address, String note, int groupid) {
		Contact contact = new Contact(userId, name, namePinyin, phone, mobile,
				email, department, nickname, address, note, groupid);
		encryptContact(contact);
		this.addContact(contact);
	}

	public void modifyContact(Contact contact) {
		encryptContact(contact);
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
	
	public Contact encryptContact(Contact contact){
		contact.setAddress(desUtil.encryptStr(contact.getAddress()));
		contact.setDepartment(desUtil.encryptStr(contact.getDepartment()));
		contact.setEmail(desUtil.encryptStr(contact.getEmail()));
		contact.setMobile(desUtil.encryptStr(contact.getMobile()));
		contact.setName(desUtil.encryptStr(contact.getName()));
		contact.setNamePinyin(desUtil.encryptStr(contact.getNamePinyin()));
		contact.setNickname(desUtil.encryptStr(contact.getNickname()));
		contact.setNote(desUtil.encryptStr(contact.getNote()));
		contact.setPhone(desUtil.encryptStr(contact.getPhone()));
		return contact;
	}
	
	public Contact decryptContact(Contact contact){
		contact.setAddress(desUtil.decryptStr(contact.getAddress()));
		contact.setDepartment(desUtil.decryptStr(contact.getDepartment()));
		contact.setEmail(desUtil.decryptStr(contact.getEmail()));
		contact.setMobile(desUtil.decryptStr(contact.getMobile()));
		contact.setName(desUtil.decryptStr(contact.getName()));
		contact.setNamePinyin(desUtil.decryptStr(contact.getNamePinyin()));
		contact.setNickname(desUtil.decryptStr(contact.getNickname()));
		contact.setNote(desUtil.decryptStr(contact.getNote()));
		contact.setPhone(desUtil.decryptStr(contact.getPhone()));
		return contact;
	}
}
