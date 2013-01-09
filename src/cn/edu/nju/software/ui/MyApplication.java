package cn.edu.nju.software.ui;

import cn.edu.nju.software.model.User;
import android.app.Application;

public class MyApplication extends Application {
	private User user;
	public static final int PAGE_SIZE = 20;
	public static final String EMAIL_ADDRESS = "androiddevelop1@163.com";
	public static final String EMAIL_PASSWORD = "1q2w3e";
	public static final String EMAIL_SEND_HOST = "smtp.163.com";
	public static final String EMAIL_RECIEVE_HOST = "imap.163.com";
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
