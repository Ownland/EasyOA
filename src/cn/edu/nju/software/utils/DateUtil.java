package cn.edu.nju.software.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public String getWeekdayByDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekdayNum = calendar.get(Calendar.DAY_OF_WEEK);
		switch(weekdayNum){
		case Calendar.SUNDAY:
			return "������";
		case Calendar.MONDAY:
			return "����һ";
		case Calendar.TUESDAY:
			return "���ڶ�";
		case Calendar.WEDNESDAY:
			return "������";
		case Calendar.THURSDAY:
			return "������";
		case Calendar.FRIDAY:
			return "������";
		case Calendar.SATURDAY:
			return "������";
		default:
			return null;
		}
	}
}
