package cn.edu.nju.software.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.edu.nju.software.adapter.ListViewCalendarDoneAdapter;
import cn.edu.nju.software.adapter.ListViewCalendarTodoAdapter;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.CalendarEventList;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.model.EmailList;
import cn.edu.nju.software.utils.NetUtil;
import cn.edu.nju.software.utils.UIHelper;
import cn.edu.nju.software.widget.PullToRefreshListView;
import cn.edu.nju.software.widget.PullToRefreshListView.OnRefreshListener;

public class CalendarActivity extends Activity{
	private static final int FROM_DATE_DIALOG_ID = 1;
	private static final int FROM_TIME_DIALOG_ID = 2;
	private static final int TO_DATE_DIALOG_ID = 3;
	private static final int TO_TIME_DIALOG_ID = 4;
	
	private Button toDoBtn;
	private Button doneBtn;
	private Button newBt;
	private Button dateFromBtn;
	private Button dateToBtn;
	private Button timeFromBtn;
	private Button timeToBtn;
	private Button calendar_new;
	private Button calendar_cancel;
	
	private EditText name;
	private EditText location;
	private EditText description;
	private CheckBox remind;
	
	
	private PullToRefreshListView calendarDoneList;
	private List<Email> listDoneCalendarList = new ArrayList<Email>();
	
	private PullToRefreshListView calendarTodoList;
	private List<Email> listTodoCalendarList = new ArrayList<Email>();
	
	private View calendarTodoFooter;
	private TextView calendarTodoFootMore;
	private ProgressBar calendarTodoFootProgress;
	private Handler calendarTodoHandler;
	private int calendarTodoSumData;
	private ListViewCalendarTodoAdapter calendarTodoAdapter;
	
	private View calendarDoneFooter;
	private TextView calendarDoneFootMore;
	private ProgressBar calendarDoneFootProgress;
	private Handler calendarDoneHandler;
	private int calendarDoneSumData;
	private ListViewCalendarDoneAdapter calendarDoneAdapter;
	
	private ScrollView calendarScroll;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_activity);
		this.initView();
	}
	public void initView(){
		this.initFrameButton();
		this.initCalendarListView();
		this.initNewCalendar();
	}
	
	public void initCalendarListView(){
		calendarTodoAdapter = new ListViewCalendarTodoAdapter(this,listTodoCalendarList,R.layout.calendars_listitem_todo);
		calendarTodoFooter = getLayoutInflater().inflate(R.layout.listview_footer_calendar_todo, null);
		calendarTodoFootMore = (TextView)calendarTodoFooter.findViewById(R.id.listview_foot_more_calendar_todo);
		calendarTodoFootProgress = (ProgressBar)calendarTodoFooter.findViewById(R.id.listview_foot_progress_calendar_todo);
		calendarTodoList = (PullToRefreshListView)findViewById(R.id.frame_listview_calendar_todo);
		calendarTodoList.addFooterView(calendarTodoFooter);
		calendarTodoList.setAdapter(calendarTodoAdapter);
		calendarTodoList.setOnItemClickListener(new CalendarTodoItemClickListener());
		calendarTodoList.setOnScrollListener(new CalendarTodoListScrollListener());
		calendarTodoList.setOnRefreshListener(new CalendarTodoListRefreshListener());
		
		calendarDoneAdapter = new ListViewCalendarDoneAdapter(this,listDoneCalendarList,R.layout.calendars_listitem_done);
		calendarDoneFooter = getLayoutInflater().inflate(R.layout.listview_footer_calendar_todo, null);
		calendarDoneFootMore = (TextView)calendarDoneFooter.findViewById(R.id.listview_foot_more_calendar_done);
		calendarDoneFootProgress = (ProgressBar)calendarDoneFooter.findViewById(R.id.listview_foot_progress_calendar_done);
		calendarDoneList = (PullToRefreshListView)findViewById(R.id.frame_listview_calendar_done);
		calendarDoneList.addFooterView(calendarDoneFooter);
		calendarDoneList.setAdapter(calendarDoneAdapter);
		calendarDoneList.setOnItemClickListener(new CalendarDoneItemClickListener());
		calendarDoneList.setOnScrollListener(new CalendarDoneListScrollListener());
		calendarDoneList.setOnRefreshListener(new CalendarDoneListRefreshListener());
	}
	
	public void initCalendarListViewData(){
		calendarTodoHandler = this.getCalendarTodoHandler(calendarTodoList, calendarTodoAdapter, calendarTodoFootMore, calendarTodoFootProgress, MyApplication.PAGE_SIZE);
		if(listTodoCalendarList.isEmpty()) {
			loadCalendarData( 0, calendarTodoHandler, UIHelper.LISTVIEW_ACTION_INIT,true,false);
		} 
		calendarDoneHandler = this.getCalendarDoneHandler(calendarDoneList, calendarDoneAdapter, calendarDoneFootMore, calendarDoneFootProgress, MyApplication.PAGE_SIZE);
		if(listDoneCalendarList.isEmpty()) {
			loadCalendarData( 0, calendarDoneHandler, UIHelper.LISTVIEW_ACTION_INIT,false,false);
		}
	}
	private void loadCalendarData(final int pageIndex,final Handler handler,final int action,final boolean todo,final boolean isRefresh){ 
		new Thread(){
			public void run() {				
				Message msg = new Message();
				try {					
					//EmailList list = new MailServiceImpl(CalendarActivity.this).getMail(pageIndex,MyApplication.PAGE_SIZE,type,isRefresh);
					CalendarEventList list = new CalendarEventList();
					List<Calendarevent> calendarList = new ArrayList<Calendarevent>();
					Calendarevent event1 = new Calendarevent();
					event1.setName("event1");
					event1.setBeginTime(new Date());
					calendarList.add(event1);
					Calendarevent event2 = new Calendarevent();
					event2.setName("event2");
					event2.setBeginTime(new Date());
					calendarList.add(event2);
					Calendarevent event3 = new Calendarevent();
					event3.setName("event3");
					event3.setBeginTime(new Date());
					calendarList.add(event3);
					Calendarevent event4 = new Calendarevent();
					event4.setName("event4");
					event4.setBeginTime(new Date());
					calendarList.add(event4);
					list.setCalendarList(calendarList);
					msg.what = list.getPageSize();
					msg.obj = list;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_EMAIL;
				handler.sendMessage(msg);
			}
		}.start();
	} 
	private void handleCalendarTodoData(int what,Object obj,int objtype,int actiontype){
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_EMAIL:
						EmailList elist = (EmailList)obj;
						calendarTodoSumData = what;
						listTodoCalendarList.clear();
						listTodoCalendarList.addAll(elist.getEmailslist());
						break;
				}
				if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
						if(!new NetUtil(this).goodNet()){
							Toast.makeText(getApplicationContext(), R.string.netBad,Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), R.string.updatre_complete,Toast.LENGTH_SHORT).show();
						}
				}
				break;
		    case UIHelper.LISTVIEW_ACTION_SCROLL:
						switch (objtype) {
							case UIHelper.LISTVIEW_DATATYPE_EMAIL:
								EmailList list = (EmailList)obj;
								calendarTodoSumData += what;
								if(listTodoCalendarList.size() > 0){
									for(Email email1 : list.getEmailslist()){
										boolean b = false;
										for(Email email2 : listTodoCalendarList){
											if(email1.getId() == email2.getId()){
												b = true;
												break;
											}
										}
										if(!b) listTodoCalendarList.add(email1);
									}
								}else{
									listTodoCalendarList.addAll(list.getEmailslist());
								}
							break;
						}
		    break;
		}
    }
	private void handleCalendarDoneData(int what,Object obj,int objtype,int actiontype){
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_EMAIL:
						EmailList elist = (EmailList)obj;
						calendarDoneSumData = what;
						listDoneCalendarList.clear();//闁稿繐鐗婄粩濠氭⒔閵堝懎鏂ч柡鍫濐樇閺嗙喖骞戦敓锟�
						listDoneCalendarList.addAll(elist.getEmailslist());
						break;
				}
				if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
					//闁圭粯鍔楅妵姘跺棘閺夊灝顬濋弶鐐跺Г閺嗙喖骞戦敓锟�
						if(!new NetUtil(this).goodNet()){
							Toast.makeText(getApplicationContext(), R.string.netBad,Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), R.string.updatre_complete,Toast.LENGTH_SHORT).show();
						}
				}
				break;
		    case UIHelper.LISTVIEW_ACTION_SCROLL:
						switch (objtype) {
							case UIHelper.LISTVIEW_DATATYPE_EMAIL:
								EmailList list = (EmailList)obj;
								calendarDoneSumData += what;
								if(listDoneCalendarList.size() > 0){
									for(Email email1 : list.getEmailslist()){
										boolean b = false;
										for(Email email2 : listDoneCalendarList){
											if(email1.getId() == email2.getId()){
												b = true;
												break;
											}
										}
										if(!b) listDoneCalendarList.add(email1);
									}
								}else{
									listDoneCalendarList.addAll(list.getEmailslist());
								}
							break;
						}
		    break;
		}
    }
	private Handler getCalendarTodoHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					handleCalendarTodoData(msg.what, msg.obj, msg.arg2, msg.arg1);
					if(msg.what < pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					}else if(msg.what == pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}
				}
				else if(msg.what == -1){
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
				}
				if(adapter.getCount()==0){
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				}else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG){
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
    }
	
	private Handler getCalendarDoneHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					handleCalendarDoneData(msg.what, msg.obj, msg.arg2, msg.arg1);
					if(msg.what < pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					}else if(msg.what == pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}
				}
				else if(msg.what == -1){
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
				}
				if(adapter.getCount()==0){
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				}else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG){
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
    }
	
	private class CalendarTodoItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	private class CalendarTodoListScrollListener implements OnScrollListener{

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	private class CalendarTodoListRefreshListener implements OnRefreshListener{

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	
	private class CalendarDoneItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	private class CalendarDoneListScrollListener implements OnScrollListener{

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	private class CalendarDoneListRefreshListener implements OnRefreshListener{

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	
	
	
	@Override
	public Dialog onCreateDialog(int dialogId){
		Calendar calendar = Calendar.getInstance();
		switch (dialogId) { 
		       case FROM_DATE_DIALOG_ID: 
		           return new DatePickerDialog(this, mFromDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		       case FROM_TIME_DIALOG_ID:
		           return new TimePickerDialog(this, mFromTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY)+1, 0, true);
		       case TO_DATE_DIALOG_ID:
		    	   return new DatePickerDialog(this, mToDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		       case TO_TIME_DIALOG_ID:
		    	   return new TimePickerDialog(this, mToTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY)+2, 0, true);
		}
		       return null; 
	}
	 /**
	     * 日期控件的事件
	     */ 
	    private DatePickerDialog.OnDateSetListener mFromDateSetListener = new DatePickerDialog.OnDateSetListener() { 
	   
	       public void onDateSet(DatePicker view, int year, int monthOfYear, 
	              int dayOfMonth) { 
	    	   dateFromBtn.setText(new StringBuilder().append(year).append("-")
	                   .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)).append("-")
	                   .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
	       } 
	    };
	 /**
	     * 时间控件的事件
	     */ 
	    private TimePickerDialog.OnTimeSetListener mFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				// TODO Auto-generated method stub
				timeFromBtn.setText(new StringBuilder().append(hour).append(":")
			               .append((minute < 10) ? "0" + minute : minute));

			} 
	    };	    

	    /**
	     * 日期控件的事件
	     */ 
	    private DatePickerDialog.OnDateSetListener mToDateSetListener = new DatePickerDialog.OnDateSetListener() { 
	   
	       public void onDateSet(DatePicker view, int year, int monthOfYear, 
	              int dayOfMonth) { 
	    	   dateToBtn.setText(new StringBuilder().append(year).append("-")
	                   .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)).append("-")
	                   .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
	       } 
	    };
	 /**
	     * 时间控件的事件
	     */ 
	    private TimePickerDialog.OnTimeSetListener mToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				// TODO Auto-generated method stub
				timeToBtn.setText(new StringBuilder().append(hour).append(":")
			               .append((minute < 10) ? "0" + minute : minute));
			} 
	       
	    };	    

	
	public void initFrameButton(){
		toDoBtn = (Button)findViewById(R.id.frame_btn_calendar_todo);
		doneBtn = (Button)findViewById(R.id.frame_btn_calendar_done);
		newBt = (Button)findViewById(R.id.frame_btn_calendar_new);
		
		toDoBtn.setEnabled(false);
		
		toDoBtn.setOnClickListener(new ToDoBtnListener());
		doneBtn.setOnClickListener(new DoneBtnListener());
		newBt.setOnClickListener(new NewBtnListener());
		
	}
	
	@SuppressWarnings("deprecation")
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
		dateFromBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(FROM_DATE_DIALOG_ID);
			}
		});
		dateToBtn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TO_DATE_DIALOG_ID);
			}
		});
		timeFromBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(FROM_TIME_DIALOG_ID);
			}
		});
		timeToBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TO_TIME_DIALOG_ID);
			}
		});
		name = (EditText)findViewById(R.id.calendar_event_name);
		location = (EditText)findViewById(R.id.calendar_new_location);
		description = (EditText)findViewById(R.id.calendar_new_description);
		remind = (CheckBox)findViewById(R.id.remind);
		calendar_new = (Button)findViewById(R.id.calendar_new);
		calendar_cancel = (Button)findViewById(R.id.calendar_cancel);
		
		calendar_new.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String eventName = name.getText().toString();
				String eventLocation = location.getText().toString();
				String eventDescription = description.getText().toString();
				boolean eventRemind = remind.isChecked();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date beginDate = null;
				Date endDate = null;
				try {
					 beginDate = sdf.parse(dateFromBtn.getText().toString()+" "+timeFromBtn.getText().toString());
					 endDate = sdf.parse(dateToBtn.getText().toString()+" "+timeToBtn.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.e("1",sdf.format(beginDate));
				//Log.e("2",sdf.format(endDate));
				
			}
		});
		calendar_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CalendarActivity.this,CalendarActivity.class);	
				startActivity(intent);
				finish();
			}
		});
		
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
