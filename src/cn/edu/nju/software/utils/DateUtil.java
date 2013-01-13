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
			return "星期天";
		case Calendar.MONDAY:
			return "星期一";
		case Calendar.TUESDAY:
			return "星期二";
		case Calendar.WEDNESDAY:
			return "星期三";
		case Calendar.THURSDAY:
			return "星期四";
		case Calendar.FRIDAY:
			return "星期五";
		case Calendar.SATURDAY:
			return "星期六";
		default:
			return null;
		}
	}
}
