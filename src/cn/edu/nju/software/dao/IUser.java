package cn.edu.nju.software.dao;

import cn.edu.nju.software.model.User;

public interface IUser {
	public void insert(User user);

	public void delete(int id);

	public void deleteAll();

	public void update(User user);
	
	public int getType(String condition);
}
