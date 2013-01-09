package cn.edu.nju.software.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class StringUtils 
{
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 灏嗗瓧绗︿覆杞綅鏃ユ湡绫诲瀷
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		if(isEmpty(sdate))	return null;		
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	
	/**
	 * 鍒ゆ柇缁欏畾瀛楃涓叉椂闂存槸鍚︿负浠婃棩
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 鍒ゆ柇缁欏畾瀛楃涓叉槸鍚︾┖鐧戒覆銆�	 * 绌虹櫧涓叉槸鎸囩敱绌烘牸銆佸埗琛ㄧ銆佸洖杞︾銆佹崲琛岀缁勬垚鐨勫瓧绗︿覆
	 * 鑻ヨ緭鍏ュ瓧绗︿覆涓簄ull鎴栫┖瀛楃涓诧紝杩斿洖true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 鍒ゆ柇鏄笉鏄竴涓悎娉曠殑鐢靛瓙閭欢鍦板潃
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	/**
	 * 瀛楃涓茶浆鏁存暟
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * 瀵硅薄杞暣鏁�	 * @param obj
	 * @return 杞崲寮傚父杩斿洖 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	/**
	 * 瀵硅薄杞暣鏁�	 * @param obj
	 * @return 杞崲寮傚父杩斿洖 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	/**
	 * 瀛楃涓茶浆甯冨皵鍨�	 * @param b
	 * @return 杞崲寮傚父杩斿洖 false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
}