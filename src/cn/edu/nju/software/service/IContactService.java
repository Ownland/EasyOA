package cn.edu.nju.software.service;

import java.util.List;
import java.util.Map;

import cn.edu.nju.software.model.Contact;

public interface IContactService {
	public List<Contact> getContactList();
	public Map<String,Object> changeInfo(Contact new_coantact);
}
