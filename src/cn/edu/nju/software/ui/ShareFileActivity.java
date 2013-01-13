package cn.edu.nju.software.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.nju.software.file.FileOperator;
import cn.edu.nju.software.file.MIMEType;
import cn.edu.nju.software.model.Document;
import cn.edu.nju.software.utils.PathUtil;
import cn.edu.nju.software.adapter.FileAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShareFileActivity extends Activity {

	private Context context;
	private ListView lstFiles;
	private ArrayList<Document> files;
	public static Map<Integer, Boolean> isChecked;
	private RelativeLayout progress;
	private ImageButton btnDownload;
	private FileAdapter adapter;
	private String filePath = ".";
	private Notification notification;
	private NotificationManager mNotificationManager;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);

		init();
	}

	public void init() {
		context = this;
		lstFiles = (ListView) findViewById(R.id.Lsv_files);
		lstFiles.setVisibility(View.VISIBLE);
		progress = (RelativeLayout) findViewById(R.id.rl_filebar);
		btnDownload = (ImageButton) findViewById(R.id.imbDownload);
		btnDownload.setOnClickListener(new MyDownloadListener());
		files = new ArrayList<Document>();
		isChecked = new HashMap<Integer, Boolean>();
		lstFiles.setOnItemClickListener(new FileItemClickListener());
		Date date = new Date();
		Document dir1 = new Document(1, "folder1", "docx", 1, date, -1, 1, 0);
		Document dir2 = new Document(2, "folder2", "xlsx", 1, date, -1, 1, 0);
		Document dir3 = new Document(3, "folder3", "xlsx", 1, date, 1, 1, 0);
		Document dir4 = new Document(12, "folder4", "xlsx", 1, date, -1, 1, 0);
		Document file1 = new Document(4, "file1", "xlsx", 1, date, -1, 0, 0);
		Document file2 = new Document(5, "file2", "xlsx", 1, date, -1, 0, 0);
		Document file3 = new Document(6, "file3", "xlsx", 1, date, 1, 0, 0);
		Document file4 = new Document(7, "file4", "xlsx", 1, date, 1, 0, 0);
		Document file5 = new Document(8, "file5", "xlsx", 1, date, 3, 0, 0);
		Document file6 = new Document(9, "file6", "xlsx", 1, date, 3, 0, 0);
		Document file7 = new Document(10, "file1", "xlsx", 1, date, 2, 0, 0);
		Document file8 = new Document(11, "file1", "xlsx", 1, date, 2, 0, 0);

		files.add(file5);
		files.add(file6);
		files.add(file7);
		files.add(file8);
		files.add(file1);
		files.add(file2);
		files.add(file3);
		files.add(file4);
		files.add(dir1);
		files.add(dir2);
		files.add(dir3);
		files.add(dir4);
		fileList(-1);
	}

	public void fileList(final int id) {
		// progress.setVisibility(View.VISIBLE);
		new AsyncTask<String, String, String>() {
			protected String doInBackground(String... params) {
				ArrayList<Document> data = FileOperator.fileList(files, id);
				adapter = new FileAdapter(context, data);
				return null;
			}

			protected void onPostExecute(String result) {
				// progress.setVisibility(View.GONE);
				lstFiles.setAdapter(adapter);
				isChecked.clear();
				Animation anim = AnimationUtils.loadAnimation(
						ShareFileActivity.this, R.anim.zoomin);
				lstFiles.startAnimation(anim);
				super.onPostExecute(result);
			}
		}.execute();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			if (filePath.equals("."))
				finish();
			else {
				int front = PathUtil.getPathId(filePath);
				filePath = PathUtil.deletePath(filePath);
				fileList(front);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyDownloadListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Iterator<Entry<Integer, Boolean>> downFiles = isChecked.entrySet()
					.iterator();
			while (downFiles.hasNext()) {
				Entry<Integer, Boolean> file = downFiles.next();

				if (file.getValue())
					System.out.println(file.getKey());
			}
			Notification();
			Thread downloadThread = new DownloadThread();
			downloadThread.start();
		}
	}

	
	@SuppressWarnings({ "deprecation" })
	public void Notification(){
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.download;
		CharSequence tickerText = "下载管理";
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		CharSequence contentTitle = "下载文件";
		CharSequence contentText = "正在下载文件...";
		Intent notificationIntent = new Intent(ShareFileActivity.this,
				SDCardActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(1, notification);
	}
	class FileItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Document cur = (Document) arg0.getItemAtPosition(arg2);
			if (cur.getType() == 0) {
				Toast.makeText(ShareFileActivity.this, "打开" + cur.getTitle(),
						Toast.LENGTH_SHORT).show();
				Thread openFile = new OpenFileThread(cur.getPath());
				openFile.start();
			} else {
				filePath = PathUtil.addPath(filePath, cur.getParentId());
				fileList(cur.getDocId());
			}
		}

	}

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.arg1){
			case 1:
			Bundle b = msg.getData();
			String fileName = b.getString("fileName");
			String dir = Environment.getExternalStorageDirectory().getPath();
			///System.out.println(dir);
			MIMEType.openFile(fileName, dir, ShareFileActivity.this);
			break;
			case 2:
				CharSequence contentTitle = "下载文件";
				CharSequence contentText = "下载完成";
				Intent notificationIntent = new Intent(ShareFileActivity.this,
						SDCardActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
						notificationIntent, 0);
				notification.setLatestEventInfo(context, contentTitle, contentText,
						contentIntent);
				mNotificationManager.notify(1, notification);
				break;
				default:break;
			}
		}

	};

	class OpenFileThread extends Thread {
		private String filePath;

		public OpenFileThread(String path) {
			filePath = path;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Message msg = new Message();
			msg.arg1 =1;
			Bundle b = new Bundle();
			b.putString("fileName", "data2whateverlog.txt");
			msg.setData(b);
			ShareFileActivity.this.myHandler.sendMessage(msg);
		}

	}

	class DownloadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.arg1 =2;
			ShareFileActivity.this.myHandler.sendMessage(msg);
		}

	}
}
