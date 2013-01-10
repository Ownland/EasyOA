package cn.edu.nju.software.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.serviceImpl.MailServiceImpl;
import cn.edu.nju.software.utils.NetUtil;

public class EmailDetailActivity extends Activity {

	private TextView titleTv;
	private TextView senderTv;
	private TextView recieverTv;
	private TextView dateTv;
	private WebView contentTv;
	private ProgressBar progressBar;
	private MyHandler myHandler;
	private DeleteHandler deleteHandler;
	private TextView deleteing;
	static final private int DELETE_ITEM = Menu.FIRST;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_detail);
		this.initView();
		myHandler = new MyHandler();
		deleteHandler = new DeleteHandler();
	}


	public void initView(){
		titleTv = (TextView)findViewById(R.id.email_detail_title);
		senderTv = (TextView)findViewById(R.id.email_detail_sender);
		recieverTv = (TextView)findViewById(R.id.email_detail_reciever);
		dateTv = (TextView)findViewById(R.id.email_detail_date);
		contentTv = (WebView)findViewById(R.id.email_detail_content);
		progressBar = (ProgressBar)findViewById(R.id.content_progressBar);
		deleteing = (TextView)findViewById(R.id.deleting);
		
		Intent intent=getIntent();
		String title=intent.getStringExtra(Email.TITLE);
		String sender = intent.getStringExtra(Email.SENDER);
		String reciever = intent.getStringExtra(Email.RECIEVER);
		String date = intent.getStringExtra(Email.DATE);
		String content = intent.getStringExtra(Email.CONTENT);
		final int number = intent.getIntExtra(Email.ID, -1);
		final int type = intent.getIntExtra(Email.TYPE, 1);
		if(content==null){
			contentTv.setVisibility(View.GONE);
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}
		titleTv.setText(title);
		senderTv.setText(sender);
		recieverTv.setText(reciever);
		dateTv.setText(date);
		contentTv.loadDataWithBaseURL(null, content, "text/html", "utf-8",null); 
		new Thread(){
			public void run(){
				 Message msg = new Message();
		         Bundle b = new Bundle();
		         Email email = new Email();
		         email.setType(EmailType.valueOf(type));
		         email.setId(number);
		         String content = new MailServiceImpl(EmailDetailActivity.this).getEmailContent(email);
		         b.putString("content", content);
		         msg.setData(b);
		         EmailDetailActivity.this.myHandler.sendMessage(msg);
			}
		}.start();
		
		
		
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
		MenuItem menuItem = menu.add(groupId, menuItemId,
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
        		
        		progressBar.setVisibility(View.VISIBLE);
        		deleteing.setVisibility(View.VISIBLE);
        		deleteEmail(email);
	        	
        	}else{
        		Toast.makeText(getApplicationContext(), R.string.netBad,
	        		     Toast.LENGTH_SHORT).show();
        	}
            break;  
        }  
        return super.onOptionsItemSelected(item);  
    } 
	public void deleteEmail(final Email email){
		new Thread(){
			public void run(){
				new MailServiceImpl(EmailDetailActivity.this).deleteEmail(email);
				Message msg = new Message();
				deleteHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private class MyHandler extends Handler{
		@Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String content = b.getString("content");
            //Log.e("content3",content);
            contentTv.loadDataWithBaseURL(null, content, "text/html", "utf-8",null);
            contentTv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
	}
	
	private class DeleteHandler extends Handler{
		@Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            deleteing.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.delete_mail_success,
       		     Toast.LENGTH_SHORT).show();
	       	Intent newIntent = new Intent(EmailDetailActivity.this,EmailActivity.class);
	       	//newIntent.putExtra("number", number);
	       	startActivity(newIntent);
	       	finish();
        }
	}
}
