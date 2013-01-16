package cn.edu.nju.software.service;

import java.util.List;
import java.util.Map;

import cn.edu.nju.software.model.Calendarevent;

public interface ICalendareventService {
	public List<Calendarevent> getCalendareventList(Map<String,String> version_map,int owner_id);
	public String updateCalendarevent(Calendarevent new_calendarevent);
}
