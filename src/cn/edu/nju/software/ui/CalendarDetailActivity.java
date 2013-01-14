package cn.edu.nju.software.ui;

import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.utils.NetUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarDetailActivity extends Activity{
	
	static final private int DELETE_ITEM = Menu.FIRST;
	static final private int EDIT_ITEM = Menu.FIRST+1;
	
	private TextView calendarDetailTitle;
	private TextView calendarDetailFrom;
	private TextView calendarDetailFromWeekDay;
	private TextView calendarDetailFromDate;
	private TextView calendarDetailTo;
	private TextView calendarDetailToWeekDay;
	private TextView calendarDetailToDate;
	private TextView calendarDetailLocation;
	private TextView calendarDetailRemind;
	private TextView calendarDetailDescription;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_detail);
		this.initView();
	}
	public void initView(){
		calendarDetailTitle = (TextView)findViewById(R.id.calendar_detail_title);
		calendarDetailFrom = (TextView)findViewById(R.id.calendar_detail_date_from);
		calendarDetailFromWeekDay = (TextView)findViewById(R.id.calendar_detail_date_weekday);
		calendarDetailFromDate= (TextView)findViewById(R.id.calendar_detail_date);
		
		calendarDetailTo = (TextView)findViewById(R.id.calendar_detail_date_to);
		calendarDetailToWeekDay = (TextView)findViewById(R.id.calendar_detail_date_to_weekday);
		calendarDetailToDate = (TextView)findViewById(R.id.calendar_detail_date_to_date);
		
		calendarDetailLocation = (TextView)findViewById(R.id.calendar_detail_loation);
		calendarDetailRemind = (TextView)findViewById(R.id.calendar_detail_remind);
		calendarDetailDescription = (TextView)findViewById(R.id.calendar_detail_description);
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
        switch (item.getItemId()) {  
        case DELETE_ITEM:  
        	Intent intent=getIntent();
        	int number = intent.getIntExtra(Email.ID, -1);
        	int type = intent.getIntExtra(Email.TYPE, 1);
        	Email email = new Email();
        	email.setId(number);
        	email.setType(EmailType.valueOf(type));
        	
        	if(new NetUtil(this).goodNet()){
        		
	        	
        	}else{
        		Toast.makeText(getApplicationContext(), R.string.netBad,
	        		     Toast.LENGTH_SHORT).show();
        	}
            break; 
        case EDIT_ITEM:
        	break;
        }  
        return super.onOptionsItemSelected(item);  
    } 
}
