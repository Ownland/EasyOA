package cn.edu.nju.software.mgr;

import android.content.Context;
import cn.edu.nju.software.dao.IUser;
import cn.edu.nju.software.dao.UserImpl;
import cn.edu.nju.software.db.DB;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.model.User;

public class UserMgr {
	private IUser dao;
	public UserMgr(Context context){
		dao = new UserImpl(context);
	}
	
	public void addUser(User user){
		dao.insert(user);
	}
	
	public int getType(int id){
		String condition = DB.TABLES.USER.FIELDS.USERID + " = " + id;
		int result = dao.getType(condition);
		return result;
	}
	public void delUser(int id) {
		dao.delete(id);
	}
	
	public void modifyUser(User user) {
		dao.update(user);
	}
}
