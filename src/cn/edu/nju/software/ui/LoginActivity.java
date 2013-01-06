package cn.edu.nju.software.ui;

import java.util.Map;

import cn.edu.nju.software.mgr.UserMgr;
import cn.edu.nju.software.model.User;
import cn.edu.nju.software.service.ILoginService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.ui.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button mBtnLogin;
	private MyHandler myHandler;
	private EditText nameText, pwdText;
	private Context context;
	private UserMgr userMgr;
	private Dialog mDialog = null;
	private int userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		myHandler = new MyHandler();
		initView();
	}

	public void initView() {
		this.context = this;
		userMgr = new UserMgr(context);
		mBtnLogin = (Button) findViewById(R.id.login);
		nameText = (EditText)findViewById(R.id.accounts);
		pwdText = (EditText)findViewById(R.id.password);
	
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

	
	class MyLoginListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showRequestDialog();
			
			new Thread() {
				public void run() {
					User user = new User();
					user.setUsername(nameText.getText().toString());
					user.setPasswd(pwdText.getText().toString());
					Message msg = new Message();
					Bundle b = new Bundle();
					
					/**
					 * 没有服务器时注释掉这一段，直接跳转到主界面
					 */
					ILoginService ls = ClientServiceHelper.getLoginService();
					Map<String, Object> result = ls.login(user.getUsername(),user.getPasswd());
					int status = (Integer) result.get("status");
					if(status == 0){
						b.putInt("result", 0);
						user = (User)result.get("User");
						userId = user.getId();
						userMgr.modifyUser(user);
					}
					else{
						b.putInt("result", 1);
						String backMsg = (String)result.get("msg");
						b.putString("msg", backMsg);
					}			
					msg.setData(b);
					LoginActivity.this.myHandler.sendMessage(msg);
				}
			}.start();
		}
	}
	
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
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, PlazaActivity.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
				finish();
			} else {
				String errMsg = b.getString("msg");
				Toast.makeText(LoginActivity.this, errMsg,
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
