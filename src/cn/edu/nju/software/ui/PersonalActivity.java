package cn.edu.nju.software.ui;

import java.util.Map;

import cn.edu.nju.software.mgr.ContactManager;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.service.IContactService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.ui.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity {

	private ImageButton imbBack, imbSave;
	private Button addMore;
	private TextView viewName;// 姓名栏
	private EditText editPhone, editMobile, editEmail, editDepartment,
			editAddress, editNickname, editNote;
	private RelativeLayout rlAddNickname, rlAddAddress, rlAddNote;
	private Context context;// 当前上下文对象
	private LayoutInflater inflater;// 反射
	private Builder builder;// 对话框创建器
	private AlertDialog dialog;// 对话框
	private Contact nowContact = null;// 点击联系人过来的实体
	private Contact newContact = null;
	private ContactManager contactMgr;
	private Dialog mDialog = null;
	public static final int RESULT_BTN_BACK = 0;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			PersonalActivity.this.mDialog.dismiss();
			Bundle b = msg.getData();
			int status = b.getInt("status");
			if (status == 0) {
				contactMgr.modifyContact(newContact);
				initUI();
				Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_activity);

		init();

		imbBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		imbSave.setOnClickListener(new MySaveListener());

		addMore.setOnClickListener(new AddMoreListener());
	}

	private void init() {
		this.context = this;
		inflater = LayoutInflater.from(context);
		builder = new Builder(context);
		imbBack = (ImageButton) findViewById(R.id.imb_new_back_personal);
		imbSave = (ImageButton) findViewById(R.id.imb_save);
		viewName = (TextView) findViewById(R.id.name_personal);
		editPhone = (EditText) findViewById(R.id.edt_inputphone_personal);
		editMobile = (EditText) findViewById(R.id.edt_inputmobile_personal);
		editEmail = (EditText) findViewById(R.id.edt_inputemail_personal);
		editDepartment = (EditText) findViewById(R.id.edt_inputdepartment_personal);
		editAddress = (EditText) findViewById(R.id.edt_inputaddress_personal);
		editNickname = (EditText) findViewById(R.id.edt_inputnickname_personal);
		editNote = (EditText) findViewById(R.id.edt_inputnote_personal);
		rlAddNickname = (RelativeLayout) findViewById(R.id.rlNickname_personal);
		rlAddAddress = (RelativeLayout) findViewById(R.id.rlAddress_personal);
		rlAddNote = (RelativeLayout) findViewById(R.id.rlNote_personal);
		addMore = (Button) findViewById(R.id.btn_addmore);
		contactMgr = new ContactManager(context, ((NowUser) getApplication())
				.getUser().getKey());
		initUI();
	}

	public void initUI() {
		nowContact = contactMgr.getContactById(((NowUser) getApplication())
				.getUser().getContactId());
		viewName.setText(nowContact.getName());
		editPhone.setText(nowContact.getPhone());
		editMobile.setText(nowContact.getMobile());
		editEmail.setText(nowContact.getEmail());
		editDepartment.setText(nowContact.getDepartment());
		if (!nowContact.getNickname().equals("")) {
			AddNicknameView();
			((EditText) rlAddNickname
					.findViewById(R.id.edt_inputnickname_personal))
					.setText(nowContact.getNickname());
		}
		if (!nowContact.getAddress().equals("")) {
			AddAddressView();
			((EditText) rlAddAddress
					.findViewById(R.id.edt_inputaddress_personal))
					.setText(nowContact.getAddress());
		}
		if (!nowContact.getNote().equals("")) {
			AddNoteView();
			((EditText) rlAddNote.findViewById(R.id.edt_inputnote_personal))
					.setText(nowContact.getNote());
		}
	}

	public void AddNicknameView() {
		rlAddNickname.setVisibility(View.VISIBLE);
		// 点击删除按钮监听事件
		rlAddNickname.findViewById(R.id.imb_del_nickname_personal)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rlAddNickname.setVisibility(View.GONE);
						((EditText) rlAddNickname
								.findViewById(R.id.edt_inputnickname_personal))
								.setText("");
					}
				});
	}

	public void AddAddressView() {
		rlAddAddress.setVisibility(View.VISIBLE);
		// 点击删除按钮监听事件
		rlAddAddress.findViewById(R.id.imb_del_address_personal)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rlAddAddress.setVisibility(View.GONE);
						((EditText) rlAddAddress
								.findViewById(R.id.edt_inputaddress_personal))
								.setText("");
					}
				});
	}

	public void AddNoteView() {
		rlAddNote.setVisibility(View.VISIBLE);
		// 点击删除按钮监听事件
		rlAddNote.findViewById(R.id.imb_del_note_personal).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						rlAddNote.setVisibility(View.GONE);
						((EditText) rlAddNote
								.findViewById(R.id.edt_inputnote_personal))
								.setText("");
					}
				});
	}

	public void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在上传信息");
		mDialog.show();
	}

	class MySaveListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showRequestDialog();
			Thread myThread = new Thread(runnable);
			myThread.start();
		}
	}

	Runnable runnable = new Runnable() {
		public void run() {
			newContact = new Contact();
			newContact.setAddress(editAddress.getText().toString());
			newContact.setDepartment(editDepartment.getText().toString());
			newContact.setEmail(editEmail.getText().toString());
			newContact.setGroupId(nowContact.getGroupId());
			newContact.setId(nowContact.getId());
			newContact.setName(nowContact.getName());
			newContact.setNamePinyin(nowContact.getNamePinyin());
			newContact.setMobile(editMobile.getText().toString());
			newContact.setNickname(editNickname.getText().toString());
			newContact.setNote(editNote.getText().toString());
			newContact.setPhone(editPhone.getText().toString());
			IContactService cs = ClientServiceHelper.getContactService();
			Map<String, Object> result = cs.changeInfo(newContact);
			int str = (Integer) result.get("status");
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putInt("status", str);
			msg.setData(b);
			PersonalActivity.this.handler.sendMessage(msg);
		}
	};

	class AddMoreListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.dialog_newcontact_addmore,
					null);
			TextView txtAddAddress = (TextView) view
					.findViewById(R.id.txtAddAdress);
			TextView txtAddNickname = (TextView) view
					.findViewById(R.id.txtAddNickname);
			TextView txtAddNote = (TextView) view.findViewById(R.id.txtAddNote);
			builder = new AlertDialog.Builder(context);
			dialog = builder.setView(view).create();
			dialog.show();
			txtAddAddress.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AddAddressView();
					dialog.dismiss();
				}
			});
			txtAddNickname.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AddNicknameView();
					dialog.dismiss();
				}
			});
			txtAddNote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AddNoteView();
					dialog.dismiss();
				}
			});
		}
	}
}
