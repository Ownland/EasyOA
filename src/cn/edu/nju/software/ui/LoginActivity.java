package cn.edu.nju.software.ui;

import java.util.Map;

import cn.edu.nju.software.model.User;
import cn.edu.nju.software.service.ILoginService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.ui.R;
import cn.edu.nju.software.utils.NetUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String FILE_NAME = "saveUserNamw";

	private Button mBtnLogin;
	private MyHandler myHandler;
	private EditText nameText, pwdText;
	private CheckBox check;
	private Dialog mDialog = null;
	private NetUtil net;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		myHandler = new MyHandler();
		net = new NetUtil(this);
		initView();
	}

	public void initView() {
		mBtnLogin = (Button) findViewById(R.id.login);
		nameText = (EditText) findViewById(R.id.accounts);
		pwdText = (EditText) findViewById(R.id.password);
		check = (CheckBox) findViewById(R.id.auto_save_password);
		loadUser();
		mBtnLogin.setOnClickListener(new MyLoginListener());
	}

	public void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
	}

	private void loadUser() {
		SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		String userNameContent = sharedPreferences.getString("username", "");
		if (userNameContent != null && !"".equals(userNameContent)) {
			nameText.setText(userNameContent);
			check.setChecked(true);
		}
	}

	private void SaveUser() {
		SharedPreferences sp = getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		Editor spEd = sp.edit();
		if (check.isChecked()) {
			spEd.putBoolean("isSave", true);
			spEd.putString("username", nameText.getText().toString());
		} else {
			spEd.putBoolean("isSave", false);
			spEd.putString("username", "");
		}
		spEd.commit();
	}

	class MyLoginListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (!net.goodNet()) {
				Toast.makeText(LoginActivity.this, "网络不可用，请检测网络连接。",
						Toast.LENGTH_SHORT).show();
			} else {
				showRequestDialog();
				Thread myThread = new Thread(runnable);
				myThread.start();
			}
		}
	}

	Runnable runnable = new Runnable() {
		public void run() {

			User user = new User();
			user.setUsername(nameText.getText().toString());
			user.setPasswd(pwdText.getText().toString());
			Message msg = new Message();
			Bundle b = new Bundle();

			/**
			 * 没有服务器时注释掉这一段，直接跳转到主界面
			 
			ILoginService ls = ClientServiceHelper.getLoginService();
			Map<String, Object> result = ls.login(user.getUsername(),
					user.getPasswd());
			if (result == null) {
				b.putInt("result", 1);
				b.putString("msg", "连不上服务器");
			} else {
				int status = (Integer) result.get("status");
				if (status == 0) {
					b.putInt("result", 0);
					user = (User) result.get("User");
					((MyApplication) getApplication()).setUser(user);
				} else {
					b.putInt("result", 1);
					String backMsg = (String) result.get("msg");
					b.putString("msg", backMsg);
				}
			}*/
			
			user.setContactId(1);
			user.setId(1);
			user.setKey("abc");
			user.setType(1);
			((MyApplication) getApplication()).setUser(user);
			b.putInt("result", 0);
			msg.setData(b);
			LoginActivity.this.myHandler.sendMessage(msg);
		}
	};

	class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
			int result = b.getInt("result");
			LoginActivity.this.mDialog.dismiss();
			if (result == 0) {
				SaveUser();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, PlazaActivity.class);
				startActivity(intent);
				finish();
			} else {
				String errMsg = b.getString("msg");
				Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
