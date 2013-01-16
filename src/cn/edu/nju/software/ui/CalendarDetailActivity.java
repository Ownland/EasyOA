package cn.edu.nju.software.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.mgr.CalendarManager;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.utils.DateUtil;
import cn.edu.nju.software.utils.NetUtil;

public class CalendarDetailActivity extends Activity{
	
	static final private int DELETE_ITEM = Menu.FIRST;
	static final private int EDIT_ITEM = Menu.FIRST+1;
	
	private TextView calendarDetailTitle;
	private TextView calendarDetailFrom;
	private TextView calendarDetailFromWeekDay;
	private TextView calendarDetailFromDate;
	private LinearLayout toLayout;
	private TextView calendarDetailTo;
	private TextView calendarDetailToWeekDay;
	private TextView calendarDetailToDate;
	private TextView calendarDetailLocation;
	private TextView calendarDetailRemind;
	private TextView calendarDetailDescription;
	private ProgressDialog mProgressDialog;
	private DeleteHandler deleteHandler = new DeleteHandler(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_detail);
		this.initView();
	}
	@Override
	public void onPause(){
		super.onPause();
		mProgressDialog.dismiss();
	}
	public void initView(){
		calendarDetailTitle = (TextView)findViewById(R.id.calendar_detail_title);
		calendarDetailFrom = (TextView)findViewById(R.id.calendar_detail_date_from);
		calendarDetailFromWeekDay = (TextView)findViewById(R.id.calendar_detail_date_weekday);
		calendarDetailFromDate= (TextView)findViewById(R.id.calendar_detail_date);
		

		toLayout = (LinearLayout)findViewById(R.id.toLayout);
		calendarDetailTo = (TextView)findViewById(R.id.calendar_detail_date_to);
		calendarDetailToWeekDay = (TextView)findViewById(R.id.calendar_detail_date_to_weekday);
		calendarDetailToDate = (TextView)findViewById(R.id.calendar_detail_date_to_date);
		
		calendarDetailLocation = (TextView)findViewById(R.id.calendar_detail_loation);
		calendarDetailRemind = (TextView)findViewById(R.id.calendar_detail_remind);
		calendarDetailDescription = (TextView)findViewById(R.id.calendar_detail_description);
		
		Intent intent = getIntent();
		calendarDetailTitle.setText(intent.getStringExtra(Calendarevent.NAME));
		calendarDetailLocation.setText(intent.getStringExtra(Calendarevent.LOCATION));
		calendarDetailRemind.setText(intent.getBooleanExtra(Calendarevent.REMIND, true)?"开":"关");
		calendarDetailDescription.setText(intent.getStringExtra(Calendarevent.DESCRIPTION));
		
		mProgressDialog = new ProgressDialog(CalendarDetailActivity.this);
		
		Date beginTime = (Date)intent.getSerializableExtra(Calendarevent.BEGIN_TIME);
		Date endTime = (Date)intent.getSerializableExtra(Calendarevent.END_TIME);
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		String fromWekDay = new DateUtil().getWeekdayByDate(beginTime);
		String toWekDay = new DateUtil().getWeekdayByDate(endTime);
		calendarDetailFromWeekDay.setText(fromWekDay);
		if(!sdfDate.format(beginTime).equals(sdfDate.format(endTime))){
			calendarDetailFrom.setVisibility(View.VISIBLE);
			calendarDetailFromDate.setText(sdfDate.format(beginTime)+" "+sdfTime.format(beginTime));
			calendarDetailTo.setVisibility(View.VISIBLE);
			calendarDetailToWeekDay.setText(toWekDay);
			calendarDetailToDate.setText(sdfDate.format(endTime)+" "+sdfTime.format(endTime));
		}else{
			calendarDetailFrom.setVisibility(View.GONE);
			calendarDetailFromDate.setText(sdfDate.format(beginTime)+" "+sdfTime.format(beginTime)+"-"+sdfTime.format(endTime));
			calendarDetailTo.setVisibility(View.GONE);
			toLayout.setVisibility(View.GONE);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Group ID
		int groupId = 0;
		// Unique menu item identifier. Used for event handling.
		int menuItemId = DELETE_ITEM;
		// The order position of the item
		int menuItemOrder = Menu.NONE;
		// Text to be displayed for this menu item.
		int menuItemText = R.string.menu_item_delete;
		// Create the menu item and keep a reference to it.
		menu.add(groupId, menuItemId,
				menuItemOrder, menuItemText);
		menuItemId = EDIT_ITEM;
		// Text to be displayed for this menu item.
		menuItemText = R.string.menu_item_edit;
		// Create the menu item and keep a reference to it.
		menu.add(groupId, menuItemId,
				menuItemOrder, menuItemText);
		return true;
	}
	
	@Override 
    public boolean onOptionsItemSelected(MenuItem item) {  
		Intent intent=getIntent();
		switch (item.getItemId()) {
        case DELETE_ITEM:  
        	int eventId = intent.getIntExtra(Calendarevent.EVENT_ID, -1);
        	if(new NetUtil(this).goodNet()){
        		deleteCalendar(eventId);
        		}else{
        		Toast.makeText(getApplicationContext(), R.string.netBad,
	        		     Toast.LENGTH_SHORT).show();
        	}
            break; 
        case EDIT_ITEM:
        	eventId = intent.getIntExtra(Calendarevent.EVENT_ID, -1);
    		String name = intent.getStringExtra(Calendarevent.NAME);
    		String location = intent.getStringExtra(Calendarevent.LOCATION);
    		boolean remind = intent.getBooleanExtra(Calendarevent.REMIND, true);
    		String description = intent.getStringExtra(Calendarevent.DESCRIPTION);
    		Date beginTime = (Date)intent.getSerializableExtra(Calendarevent.BEGIN_TIME);
    		Date endTime = (Date)intent.getSerializableExtra(Calendarevent.END_TIME);
    		int ownerId = intent.getIntExtra(Calendarevent.OWNER_ID, 0);
    		int version = intent.getIntExtra(Calendarevent.VERSION, 1);
    		
    		Intent newIntent = new Intent(CalendarDetailActivity.this,
					CalendarEditActivity.class);
    		newIntent.putExtra(Calendarevent.EVENT_ID, eventId);
    		newIntent.putExtra(Calendarevent.BEGIN_TIME, beginTime);
    		newIntent.putExtra(Calendarevent.END_TIME,endTime);
    		newIntent.putExtra(Calendarevent.NAME, name);
    		newIntent.putExtra(Calendarevent.LOCATION, location);
    		newIntent.putExtra(Calendarevent.DESCRIPTION,description);
    		newIntent.putExtra(Calendarevent.REMIND, remind);
    		newIntent.putExtra(Calendarevent.OWNER_ID, ownerId);
    		newIntent.putExtra(Calendarevent.VERSION, version);
			startActivity(newIntent);
    		finish();
        	break;
        }  
        return super.onOptionsItemSelected(item);  
    } 
	public void deleteCalendar(final int eventId){
		mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("请稍等。。。");
		mProgressDialog.setMessage("正在删除日历");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		new Thread(){
			public void run(){
				new CalendarManager(CalendarDetailActivity.this).deleteCalendar(eventId);
				CalendarDetailActivity.this.deleteHandler.sendEmptyMessage(1);
			}
		}.start();
	}
	
	private class DeleteHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			Intent intent = new Intent(CalendarDetailActivity.this,CalendarActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
