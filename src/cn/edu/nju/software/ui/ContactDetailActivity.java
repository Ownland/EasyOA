package cn.edu.nju.software.ui;

import cn.edu.nju.software.mgr.ContactManager;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.ui.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {

	private ImageButton imbBack, imbCall, imbMsg, imbEmail;
	private TextView viewName;// 姓名栏
	private TextView phoneNum, mobileNum;
	private EditText editPhone, editMobile, editEmail, editDepartment;
	private RelativeLayout rlAddNickname, rlAddAddress, rlAddNote;
	private PopupWindow popWin;
	private Context context;// 当前上下文对象
	private int nowContactId;// 点击联系人传递过来的联系人ID号
	private Contact nowContact = null;// 点击联系人过来的实体
	private ContactManager contactMgr;

	public static final int RESULT_BTN_BACK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetail_activity);

		init();

		imbBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		
		imbCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initPopWindow();
				popWin.showAsDropDown(findViewById(R.id.imb_contact_call),-50,0);
			}

		});
		
		imbMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri smsToUri = Uri.parse("smsto:"+nowContact.getMobile());
				Intent intent = new Intent(android.content.Intent.ACTION_SENDTO,smsToUri);
				startActivity(intent);
			}

		});
		
		imbEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
	}

	private void init() {
		this.context = this;
		nowContactId = this.getIntent().getIntExtra("id", -1);
		imbBack = (ImageButton) findViewById(R.id.imb_new_back);
		imbCall = (ImageButton) findViewById(R.id.imb_contact_call);
		imbMsg = (ImageButton) findViewById(R.id.imb_contact_msg);
		imbEmail = (ImageButton) findViewById(R.id.imb_contact_email);
		viewName = (TextView) findViewById(R.id.name);
		editPhone = (EditText) findViewById(R.id.edt_inputphone);
		editMobile = (EditText) findViewById(R.id.edt_inputmobile);
		editEmail = (EditText) findViewById(R.id.edt_inputemail);
		editDepartment = (EditText) findViewById(R.id.edt_inputdepartment);
		rlAddNickname = (RelativeLayout) findViewById(R.id.rlNickname);
		rlAddAddress = (RelativeLayout) findViewById(R.id.rlAddress);
		rlAddNote = (RelativeLayout) findViewById(R.id.rlNote);
		contactMgr = new ContactManager(context);
		initUI();
	}

	public void initUI() {
		nowContact = contactMgr.getContactById(nowContactId);
		viewName.setText(nowContact.getName());
		editPhone.setText(nowContact.getPhone());
		editMobile.setText(nowContact.getMobile());
		editEmail.setText(nowContact.getEmail());
		editDepartment.setText(nowContact.getDepartment());
		if (!nowContact.getNickname().equals("")) {
			rlAddNickname.setVisibility(View.VISIBLE);
			((EditText) rlAddNickname.findViewById(R.id.edt_inputnickname))
					.setText(nowContact.getNickname());
		}
		if (!nowContact.getAddress().equals("")) {
			rlAddAddress.setVisibility(View.VISIBLE);
			((EditText) rlAddAddress.findViewById(R.id.edt_inputaddress))
					.setText(nowContact.getAddress());
		}
		if (!nowContact.getNote().equals("")) {
			rlAddNote.setVisibility(View.VISIBLE);
			((EditText) rlAddNote.findViewById(R.id.edt_inputnote))
					.setText(nowContact.getNote());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void initPopWindow(){
		if(popWin == null){
			View view = getLayoutInflater().inflate(R.layout.popup_call, null);
			phoneNum = (TextView)view.findViewById(R.id.phone_num);
			mobileNum = (TextView)view.findViewById(R.id.mobile_num);
			phoneNum.setText(nowContact.getPhone());
			mobileNum.setText(nowContact.getMobile());
			phoneNum.setOnClickListener(new CallListener(nowContact.getPhone()));
			mobileNum.setOnClickListener(new CallListener(nowContact.getMobile()));
			
			popWin = new PopupWindow(view,LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			popWin.setFocusable(true);
			popWin.setTouchable(true);
			popWin.setBackgroundDrawable(new BitmapDrawable());
		}
		if(popWin.isShowing()){
			popWin.dismiss();
		}
	}
	
	class CallListener implements OnClickListener{
		
		private String num;
		public CallListener(String phone) {
			// TODO Auto-generated constructor stub
			num = phone;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			popWin.dismiss();
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + num));
			startActivity(intent);
		}
		
	}
}
