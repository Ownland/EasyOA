package cn.edu.nju.software.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;

public class CalendarActivity extends Activity{
	
	private Button toDoBtn;
	private Button doneBtn;
	private Button newBt;
	private Button dateFromBtn;
	private Button dateToBtn;
	private Button timeFromBtn;
	private Button timeToBtn;
	
	private ScrollView calendarScroll;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_activity);
		this.initView();
	}
	
	public void initView(){
		this.initFrameButton();
		
		this.initNewCalendar();
	}
	
	public void initFrameButton(){
		toDoBtn = (Button)findViewById(R.id.frame_btn_calendar_todo);
		doneBtn = (Button)findViewById(R.id.frame_btn_calendar_done);
		newBt = (Button)findViewById(R.id.frame_btn_calendar_new);
		
		toDoBtn.setEnabled(false);
		
		toDoBtn.setOnClickListener(new ToDoBtnListener());
		doneBtn.setOnClickListener(new DoneBtnListener());
		newBt.setOnClickListener(new NewBtnListener());
		
	}
	
	public void initNewCalendar(){
		calendarScroll = (ScrollView)findViewById(R.id.calendar_scroll);
		dateFromBtn = (Button)findViewById(R.id.date_btn_from);
		dateToBtn = (Button)findViewById(R.id.date_btn_to);
		timeFromBtn = (Button)findViewById(R.id.time_btn_from);
		timeToBtn = (Button)findViewById(R.id.time_btn_to);
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		dateFromBtn.setText(sdfDate.format(new Date()));
		dateToBtn.setText(sdfDate.format(new Date()));
		timeFromBtn.setText((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1)+":00");
		timeToBtn.setText((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+2)+":00");
	}
	
	private class ToDoBtnListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			toDoBtn.setEnabled(false);
			doneBtn.setEnabled(true);
			newBt.setEnabled(true);
			calendarScroll.setVisibility(View.GONE);
		}

		
		
	}
	
	private class DoneBtnListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			toDoBtn.setEnabled(true);
			doneBtn.setEnabled(false);
			newBt.setEnabled(true);
			calendarScroll.setVisibility(View.GONE);
		}

		
		
	}
	
	private class NewBtnListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			toDoBtn.setEnabled(true);
			doneBtn.setEnabled(true);
			newBt.setEnabled(false);
			calendarScroll.setVisibility(View.VISIBLE);
		}

		
	}
}
