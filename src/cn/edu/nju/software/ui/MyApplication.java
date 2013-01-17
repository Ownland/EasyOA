package cn.edu.nju.software.ui;

import cn.edu.nju.software.model.User;
import android.app.Application;

public class MyApplication extends Application {
	private User user;
	public static final int PAGE_SIZE = 20;
	public static final String EMAIL_ADDRESS = "ysm@localhost";
	public static final String EMAIL_PASSWORD = "123456";
	public static final String EMAIL_SEND_HOST = "192.168.1.100";
	public static final String EMAIL_RECIEVE_HOST = "192.168.1.100";
	public static final String SD_DIR = "/mnt/sdcard/EasyOA";
	public static final String TEMP_DIR = "/mnt/sdcard/temp/EasyOA";
	@Override  
    public void onCreate() {  
        super.onCreate();  
    }  
  
    @Override  
    public void onTerminate() {  
        super.onTerminate();  
    }  
    
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
