package cn.edu.nju.software.ui;

import android.app.Activity;
import android.os.Bundle;

public class CalendarActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_activity);
		this.initView();
	}
	
	public void initView(){
		
	}
}
