package cn.edu.nju.software.service;

import java.util.Map;

public interface ILoginService {
	public Map<String, Object> login(String username, String password);
}
