package cn.edu.nju.software.ui;

import cn.edu.nju.software.model.User;
import android.app.Application;

public class MyApplication extends Application {
	private User user;

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
