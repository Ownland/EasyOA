package cn.edu.nju.software.dao;

import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;

public interface EmailDAO {
	public void insert(Email email);
	public void delete(Email email);
	public Email findEmailByNumber(int number,EmailType type);
	public void update(Email email,int number,EmailType type);
	public int findMaxNumber(EmailType type);
	public int findMinNumber(EmailType type);
	
}
