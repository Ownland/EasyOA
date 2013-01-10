package cn.edu.nju.software.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.adapter.ListViewEmailAdapter;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.model.EmailList;
import cn.edu.nju.software.serviceImpl.MailServiceImpl;
import cn.edu.nju.software.utils.NetUtil;
import cn.edu.nju.software.utils.StringUtils;
import cn.edu.nju.software.utils.UIHelper;
import cn.edu.nju.software.widget.PullToRefreshListView;
import cn.edu.nju.software.widget.PullToRefreshListView.OnRefreshListener;

public class EmailActivity extends Activity{
	private Button frameBtEmailInbox;
	private Button frameBtEmailOutbox;
	private Button frameBtEmailNew;
	
	private FrameLayout searchFrame;
	private Button searchBt;
	private EditText searchText;
	
	private PullToRefreshListView emailRevievedList;
	private List<Email> listRecievedEmail = new ArrayList<Email>();
	private ListViewEmailAdapter emailAdapter;
	
	private Button sendBt;
	private Button cancelBt;
	private EditText recieverText;
	private EditText titleText;
	private EditText contentText;
	
	private View emailFooter;
	private TextView emailFootMore;
	private ProgressBar emailFootProgress;
	private Handler emailHandler;
	
	private int emailsSumData;
	
	private EmailType curEmailType = EmailType.INBOXMAIL;
	
	private ScrollView scrollView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_activity);
		initView();
	}
	
	public void initView(){
		this.initFrameButton();
		this.initSearchFrame();
		this.initEmailListView();
		this.initNewEmailView();
		this.initEmailListViewData();
	}
	public void initFrameButton(){
		frameBtEmailInbox = (Button)findViewById(R.id.frame_btn_email_inbox);
		frameBtEmailOutbox = (Button)findViewById(R.id.frame_btn_email_outbox);
		frameBtEmailNew = (Button)findViewById(R.id.frame_btn_email_new);
		
		frameBtEmailInbox.setEnabled(false);
		
		frameBtEmailInbox.setOnClickListener(new FrameBtEmailInboxListener());
		frameBtEmailOutbox.setOnClickListener(new FrameBtEmailOutboxListener());
		frameBtEmailNew.setOnClickListener(new FrameBtEmailNewListener());
		
	}
	
	public void initSearchFrame(){
		searchFrame = (FrameLayout)findViewById(R.id.search_frame);
		searchBt = (Button)findViewById(R.id.search_btn);
		searchText = (EditText)findViewById(R.id.search_editer);
	}
	
	public void initEmailListView(){
		emailAdapter = new ListViewEmailAdapter(this,listRecievedEmail,R.layout.emails_listitem);
		emailFooter = getLayoutInflater().inflate(R.layout.listview_footer, null);
		emailFootMore = (TextView)emailFooter.findViewById(R.id.listview_foot_more);
		emailFootProgress = (ProgressBar)emailFooter.findViewById(R.id.listview_foot_progress);
		emailRevievedList = (PullToRefreshListView)findViewById(R.id.frame_listview_emails_recieve);
		emailRevievedList.addFooterView(emailFooter);
		emailRevievedList.setAdapter(emailAdapter);
		emailRevievedList.setOnItemClickListener(new EmailItemClickListener());
		emailRevievedList.setOnScrollListener(new EmailListScrollListener());
		emailRevievedList.setOnRefreshListener(new EmailListRefreshListener());
	}
	
	public void initNewEmailView(){
		scrollView = (ScrollView)findViewById(R.id.email_scroll);
		sendBt = (Button)findViewById(R.id.email_send);
		cancelBt = (Button)findViewById(R.id.email_cancel);
		cancelBt = (Button)findViewById(R.id.email_cancel);
		recieverText = (EditText)findViewById(R.id.email_new_reciever);
		titleText = (EditText)findViewById(R.id.email_new_title);
		contentText = (EditText)findViewById(R.id.email_new_content);
		
		sendBt.setOnClickListener(new SendBtListener());
		cancelBt.setOnClickListener(new CancelBtListener());
	}
	
	private class CancelBtListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(EmailActivity.this,EmailActivity.class);
			startActivity(intent);
		}
		
	}
	
	private class SendBtListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Email email = new Email();
			String title = titleText.getText().toString();
			String reciever = recieverText.getText().toString();
			String content = contentText.getText().toString();
			boolean networkAvailable = new NetUtil(EmailActivity.this).goodNet();
			if(networkAvailable){
				if(reciever.equals("")){
					Toast.makeText(EmailActivity.this, R.string.reciever_empty, Toast.LENGTH_SHORT).show();
				}else{
					email.setTitle(title);
					email.setSender(MyApplication.EMAIL_ADDRESS);
					email.setReciever(reciever);
					email.setContent(content);
					sendEmail(email);
					Toast.makeText(EmailActivity.this,R.string.email_send_success, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(EmailActivity.this,EmailActivity.class);
					startActivity(intent);
					finish();
				}
			}else{
				Toast.makeText(EmailActivity.this,R.string.netBad, Toast.LENGTH_SHORT).show();
				
			}
		}
		
	}
	
	public void sendEmail(final Email email){
		new Thread(){
				public void run(){
					new MailServiceImpl(EmailActivity.this).sendEmail(email);
				}
		}.start();
	}
	
	public void initEmailListViewData(){
		emailHandler = this.getHandler(emailRevievedList, emailAdapter, emailFootMore, emailFootProgress, MyApplication.PAGE_SIZE);
		if(listRecievedEmail.isEmpty()) {
			loadEmailData( 0, emailHandler, UIHelper.LISTVIEW_ACTION_INIT,EmailType.INBOXMAIL,false);
		}   
	}
	private Handler getHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					handleEmailData(msg.what, msg.obj, msg.arg2, msg.arg1);
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
	private void handleEmailData(int what,Object obj,int objtype,int actiontype){
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_EMAIL:
						EmailList elist = (EmailList)obj;
						emailsSumData = what;
					   listRecievedEmail.clear();//闁稿繐鐗婄粩濠氭⒔閵堝懎鏂ч柡鍫濐樇閺嗙喖骞戦敓锟�
					   listRecievedEmail.addAll(elist.getEmailslist());
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
								emailsSumData += what;
								if(listRecievedEmail.size() > 0){
									for(Email email1 : list.getEmailslist()){
										boolean b = false;
										for(Email email2 : listRecievedEmail){
											if(email1.getId() == email2.getId()){
												b = true;
												break;
											}
										}
										if(!b) listRecievedEmail.add(email1);
									}
								}else{
									listRecievedEmail.addAll(list.getEmailslist());
								}
							break;
						}
		    break;
		}
    }
	private class FrameBtEmailInboxListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			curEmailType = EmailType.INBOXMAIL;
			frameBtEmailInbox.setEnabled(false);
			frameBtEmailOutbox.setEnabled(true);
			frameBtEmailNew.setEnabled(true);
			
			searchFrame.setVisibility(View.VISIBLE);
			searchText.setText("");
			
			scrollView.setVisibility(View.GONE);
			emailRevievedList.setVisibility(View.VISIBLE);
			
			loadEmailData( 0, emailHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG,curEmailType,false);
		}
		
	}
	
	private class FrameBtEmailOutboxListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			curEmailType = EmailType.OUTBOXMAIL;
			frameBtEmailInbox.setEnabled(true);
			frameBtEmailOutbox.setEnabled(false);
			frameBtEmailNew.setEnabled(true);
			searchFrame.setVisibility(View.VISIBLE);
			searchText.setText("");
			scrollView.setVisibility(View.GONE);
			emailRevievedList.setVisibility(View.VISIBLE);
			
			loadEmailData( 0, emailHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG,curEmailType,false);
		}
	}
	
	private class FrameBtEmailNewListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			frameBtEmailInbox.setEnabled(true);
			frameBtEmailOutbox.setEnabled(true);
			frameBtEmailNew.setEnabled(false);
			searchFrame.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
			emailRevievedList.setVisibility(View.GONE);
		}
	}
	
	private class EmailItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(position == 0 || view == emailFooter) return;
			Email email = null;        		
    		if(view instanceof TextView){
    			email = (Email)view.getTag();
    		}else{
    			TextView tv = (TextView)view.findViewById(R.id.email_listitem_title);
    			email = (Email)tv.getTag();
    		}
    		if(email == null) return;
    		
			Intent intent = new Intent(EmailActivity.this,EmailDetailActivity.class);
			intent.putExtra(Email.TITLE,email.getTitle());
			intent.putExtra(Email.SENDER,email.getSender());
			intent.putExtra(Email.RECIEVER,email.getReciever());
			intent.putExtra(Email.CONTENT,email.getContent());
			intent.putExtra(Email.TYPE, email.getType().value());
			intent.putExtra(Email.ID, email.getId());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			intent.putExtra(Email.DATE,df.format(email.getDate()));
			startActivity(intent);
		}
		
	}
	
	private class EmailListScrollListener implements OnScrollListener{

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
				emailRevievedList.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			emailRevievedList.onScrollStateChanged(view, scrollState);
			if(listRecievedEmail.isEmpty()) return;
			boolean scrollEnd = false;
			try {
				if(view.getPositionForView(emailFooter) == view.getLastVisiblePosition())
					scrollEnd = true;
			} catch (Exception e) {
				scrollEnd = false;
			}
			int lvDataState = StringUtils.toInt(emailRevievedList.getTag());
			if(scrollEnd && lvDataState==UIHelper.LISTVIEW_DATA_MORE)
			{
				emailRevievedList.setTag(UIHelper.LISTVIEW_DATA_LOADING);
				emailFootMore.setText(R.string.load_ing);
				emailFootProgress.setVisibility(View.VISIBLE);
				//鐟滅増鎸告晶鐖宎geIndex
				int pageIndex = emailsSumData/MyApplication.PAGE_SIZE;
				Log.e("pageIndex",pageIndex+"");
				loadEmailData( pageIndex, emailHandler, UIHelper.LISTVIEW_ACTION_SCROLL,curEmailType,true);
			}
		}
		
	}
	
	private class EmailListRefreshListener implements OnRefreshListener{

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
				loadEmailData( 0, emailHandler, UIHelper.LISTVIEW_ACTION_REFRESH,curEmailType,true);
		}
		
	}
	
	private void loadEmailData(final int pageIndex,final Handler handler,final int action,final EmailType type,final boolean isRefresh){ 
		new Thread(){
			public void run() {				
				Message msg = new Message();
				try {					
					EmailList list = new MailServiceImpl(EmailActivity.this).getMail(pageIndex,MyApplication.PAGE_SIZE,type,isRefresh);
					msg.what = list.getPageSize();
					msg.obj = list;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_EMAIL;
				if(curEmailType == type){
					handler.sendMessage(msg);
				}
			}
		}.start();
	} 
	
}
