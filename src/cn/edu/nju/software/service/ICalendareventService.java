package cn.edu.nju.software.service;

import java.util.List;
import java.util.Map;

import cn.edu.nju.software.model.Calendarevent;

public interface ICalendareventService {
	public List<Calendarevent> getCalendareventList(Map<String,String> version_map,int owner_id);
	public Map<String,Object> updateCalendarevent(Calendarevent new_calendarevent);
	public Map<String,Object> deleteCalendarevent(int event_id,int owner_id);
	public Map<String,Object> addCalendarevent(Calendarevent new_calendarevent);
}
