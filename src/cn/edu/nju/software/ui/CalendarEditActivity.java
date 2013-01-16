package cn.edu.nju.software.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.edu.nju.software.mgr.CalendarManager;
import cn.edu.nju.software.model.Calendarevent;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CalendarEditActivity extends Activity{
	private static final int FROM_DATE_DIALOG_ID = 1;
	private static final int FROM_TIME_DIALOG_ID = 2;
	private static final int TO_DATE_DIALOG_ID = 3;
	private static final int TO_TIME_DIALOG_ID = 4;
	
	private Button dateFromBtn;
	private Button dateToBtn;
	private Button timeFromBtn;
	private Button timeToBtn;
	private Button calendar_save;
	private Button calendar_cancel;
	private EditText name;
	private EditText location;
	private EditText description;
	private CheckBox remind;
	private EditHandler editHandler = new EditHandler();
	private ProgressDialog mProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_edit);
		this.initView();
	}
	@Override
	public Dialog onCreateDialog(int dialogId) {
		Calendar calendar = Calendar.getInstance();
		switch (dialogId) {
		case FROM_DATE_DIALOG_ID:
			return new DatePickerDialog(this, mFromDateSetListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DATE));
		case FROM_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mFromTimeSetListener,
					Integer.parseInt(timeFromBtn.getText().toString().split(":")[0]), 0, true);
		case TO_DATE_DIALOG_ID:
			return new DatePickerDialog(this, mToDateSetListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DATE));
		case TO_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mToTimeSetListener,
					Integer.parseInt(timeToBtn.getText().toString().split(":")[0]), 0, true);
		}
		return null;
	}
	@Override
	public void onPause(){
		super.onPause();
		mProgressDialog.dismiss();
	}
	public void initView(){
		dateFromBtn = (Button)findViewById(R.id.date_btn_from_edit);
		dateToBtn = (Button)findViewById(R.id.date_btn_to_edit);
		timeFromBtn = (Button)findViewById(R.id.time_btn_from_edit);
		timeToBtn = (Button)findViewById(R.id.time_btn_to_edit);
		name = (EditText)findViewById(R.id.calendar_event__name_edit);
		location = (EditText)findViewById(R.id.calendar_new_location_edit);
		description = (EditText)findViewById(R.id.calendar_new_description_edit);
		remind = (CheckBox)findViewById(R.id.remind_edit);
		calendar_save = (Button)findViewById(R.id.calendar_edit);
		calendar_cancel = (Button)findViewById(R.id.calendar_cancel_edit);
		
		Intent intent = getIntent();
		
		int eventId = intent.getIntExtra(Calendarevent.EVENT_ID, -1);
		String eName = intent.getStringExtra(Calendarevent.NAME);
		String eLocation = intent.getStringExtra(Calendarevent.LOCATION);
		boolean eRemind = intent.getBooleanExtra(Calendarevent.REMIND, true);
		String eDescription = intent.getStringExtra(Calendarevent.DESCRIPTION);
		Date beginTime = (Date)intent.getSerializableExtra(Calendarevent.BEGIN_TIME);
		Date endTime = (Date)intent.getSerializableExtra(Calendarevent.END_TIME);
		int ownerId = intent.getIntExtra(Calendarevent.OWNER_ID, 0);
		int version = intent.getIntExtra(Calendarevent.VERSION, 1);
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		dateFromBtn.setText(sdfDate.format(beginTime));
		dateToBtn.setText(sdfDate.format(endTime));
		mProgressDialog = new ProgressDialog(
					CalendarEditActivity.this);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int fromHour = 0;
		int toHour = 0;
		
		timeFromBtn
				.setText(sdfTime.format(beginTime));
		timeToBtn
				.setText(sdfTime.format(endTime));
		name.setText(eName);
		location.setText(eLocation);
		description.setText(eDescription);
		remind.setChecked(eRemind);
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
		calendar_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				int eventId = intent.getIntExtra(Calendarevent.EVENT_ID, -1);
				int ownerId = intent.getIntExtra(Calendarevent.OWNER_ID, 0);
				int version = intent.getIntExtra(Calendarevent.VERSION, 1);
				String eventName = name.getText().toString();
				String eventLocation = location.getText().toString();
				String eventDescription = description.getText().toString();
				boolean eventRemind = remind.isChecked();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date beginDate = null;
				Date endDate = null;
				try {
					beginDate = sdf.parse(dateFromBtn.getText().toString()
							+ " " + timeFromBtn.getText().toString());
					endDate = sdf.parse(dateToBtn.getText().toString() + " "
							+ timeToBtn.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Log.e("1",sdf.format(beginDate));
				// Log.e("2",sdf.format(endDate));
				Calendarevent calendar = new Calendarevent();
				calendar.setEventId(eventId);
				calendar.setBeginTime(beginDate);
				calendar.setEndTime(endDate);
				calendar.setDescription(eventDescription);
				calendar.setLocation(eventLocation);
				calendar.setName(eventName);
				calendar.setRemind(eventRemind);
				calendar.setVersion(version);
				calendar.setOwnerId(ownerId);
				editCalendar(calendar);

			}
		});
		
		calendar_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CalendarEditActivity.this,
						CalendarActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	private DatePickerDialog.OnDateSetListener mFromDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateFromBtn.setText(new StringBuilder()
					.append(year)
					.append("-")
					.append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
							: (monthOfYear + 1)).append("-")
					.append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
		}
	};
	/**
	 * éƒå •æ£¿éŽºÑ‚æ¬¢é¨å‹ªç°¨æµ ï¿½
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
	 * éƒãƒ¦æ¹¡éŽºÑ‚æ¬¢é¨å‹ªç°¨æµ ï¿½
	 */
	private DatePickerDialog.OnDateSetListener mToDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateToBtn.setText(new StringBuilder()
					.append(year)
					.append("-")
					.append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
							: (monthOfYear + 1)).append("-")
					.append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
		}
	};
	/**
	 * éƒå •æ£¿éŽºÑ‚æ¬¢é¨å‹ªç°¨æµ ï¿½
	 */
	private TimePickerDialog.OnTimeSetListener mToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			// TODO Auto-generated method stub
			timeToBtn.setText(new StringBuilder().append(hour).append(":")
					.append((minute < 10) ? "0" + minute : minute));
		}

	};
	public void editCalendar(final Calendarevent calendar) {
		
		mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("ÇëÉÔµÈ¡£¡£¡£");
		mProgressDialog.setMessage("ÕýÔÚ±£´æÈÕÀú");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		new Thread() {
			public void run() {
				new CalendarManager(CalendarEditActivity.this)
						.updateCalendar(calendar);
				CalendarEditActivity.this.editHandler.sendEmptyMessage(1);
			}
		}.start();
	}
	private class EditHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(CalendarEditActivity.this,
					CalendarActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
