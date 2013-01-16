package cn.edu.nju.software.ui;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.nju.software.file.FileOperator;
import cn.edu.nju.software.file.MIMEType;
import cn.edu.nju.software.model.Document;
import cn.edu.nju.software.service.IDocumentService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.utils.FileUtils;
import cn.edu.nju.software.utils.NetUtil;
import cn.edu.nju.software.utils.PathUtil;
import cn.edu.nju.software.adapter.FileAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
	private ImageButton btnSD;
	private FileAdapter adapter;
	private String filePath = ".";
	private Notification notification;
	private NotificationManager mNotificationManager;
	private Dialog mDialog = null;
	private NetUtil net;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);

		init();
	}

	@SuppressLint("UseSparseArrays")
	public void init() {
		context = this;
		net = new NetUtil(context);
		lstFiles = (ListView) findViewById(R.id.Lsv_files);
		lstFiles.setVisibility(View.VISIBLE);
		progress = (RelativeLayout) findViewById(R.id.rl_filebar);
		btnDownload = (ImageButton) findViewById(R.id.imbDownload);
		btnSD = (ImageButton) findViewById(R.id.imbSD);
		btnSD.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ShareFileActivity.this, SDCardActivity.class);
				startActivity(intent);
			}
			
		});
		btnDownload.setOnClickListener(new MyDownloadListener());
		//files = new ArrayList<Document>();
		isChecked = new HashMap<Integer, Boolean>();
		lstFiles.setOnItemClickListener(new FileItemClickListener());
		/*Date date = new Date();
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
		fileList(-1);*/

		initDocuments();
	}

	public void initDocuments() {
		lstFiles.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		if (!net.goodNet()) {
			Toast.makeText(ShareFileActivity.this, "网络不可用，请检测网络连接。",
					Toast.LENGTH_SHORT).show();
			lstFiles.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
		} else {
			Thread initDoc = new DocumentThread();
			initDoc.start();
		}
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

	public void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在打开...");
		mDialog.show();
	}

	class MyDownloadListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!net.goodNet()) {
				Toast.makeText(ShareFileActivity.this, "网络不可用，请检测网络连接。",
						Toast.LENGTH_SHORT).show();
			} else {
				ArrayList<Integer> checkInt = new ArrayList<Integer>();
				Iterator<Entry<Integer, Boolean>> downFiles = isChecked
						.entrySet().iterator();
				while (downFiles.hasNext()) {
					Entry<Integer, Boolean> file = downFiles.next();
					if (file.getValue())
						checkInt.add(file.getKey());
				}
				if (0 == checkInt.size()) {
					Toast.makeText(ShareFileActivity.this, "请至少选择一个需要下载的文件。",
							Toast.LENGTH_SHORT).show();
				} else {
					Notification();
					Thread downloadThread = new DownloadThread(checkInt, files);
					downloadThread.start();
				}
			}
		}
	}

	@SuppressWarnings({ "deprecation" })
	public void Notification() {
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
				if (!net.goodNet()) {
					Toast.makeText(ShareFileActivity.this, "网络不可用，请检测网络连接。",
							Toast.LENGTH_SHORT).show();
				} else {
					showRequestDialog();
					Thread openFile = new OpenFileThread(cur.getDocId());
					openFile.start();
				}
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
			switch (msg.arg1) {
			case 0:
				if(0 == msg.arg2)
					fileList(-1);
				else
					Toast.makeText(ShareFileActivity.this, "连接服务器失败。",
							Toast.LENGTH_SHORT).show();
				lstFiles.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				break;
			case 1:
				ShareFileActivity.this.mDialog.dismiss();
				Bundle b = msg.getData();
				String fileName = b.getString("fileName");
				String dir = Environment.getDownloadCacheDirectory().getPath();
				MIMEType.openFile(fileName, dir, ShareFileActivity.this);
				break;
			case 2:
				CharSequence contentTitle = "下载文件";
				CharSequence contentText = "下载完成";
				Intent notificationIntent = new Intent(ShareFileActivity.this,
						SDCardActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, notificationIntent, 0);
				notification.setLatestEventInfo(context, contentTitle,
						contentText, contentIntent);
				mNotificationManager.notify(1, notification);
				break;
			default:
				break;
			}
		}

	};

	class DocumentThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			IDocumentService ds = ClientServiceHelper.getDocumentService();
			files = (ArrayList<Document>) ds.getDocumentList();
			Message msg = new Message();
			msg.arg1 = 0;
			if(files == null){			
				msg.arg2 = 1;
			} else{
				msg.arg2 = 0;
			}
			
			ShareFileActivity.this.myHandler.sendMessage(msg);
		}

	}

	class OpenFileThread extends Thread {
		private int docId;

		public OpenFileThread(int id) {
			this.docId = id;;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			IDocumentService ds = ClientServiceHelper.getDocumentService();
			ByteArrayInputStream bis = ds.getDocumentContent(docId);
			String path = Environment.getDownloadCacheDirectory().getPath();
			FileUtils
					.saveFile(
							bis,
							path
									+ "/"
									+ filePath.substring(filePath
											.lastIndexOf("/") + 1));
			Message msg = new Message();
			msg.arg1 = 1;
			Bundle b = new Bundle();
			b.putString("fileName",
					filePath.substring(filePath.lastIndexOf("/") + 1));
			msg.setData(b);
			ShareFileActivity.this.myHandler.sendMessage(msg);
		}

	}

	class DownloadThread extends Thread {
		ArrayList<Integer> ids;
		ArrayList<Document> docs;

		public DownloadThread(ArrayList<Integer> ids, ArrayList<Document> docs) {
			this.ids = ids;
			this.docs = docs;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			/*ArrayList<String> names = new ArrayList<String>();
			for (int i = 0; i < docs.size(); i++) {
				if (ids.contains(docs.get(i).getDocId()))
					names.add(docs.get(i).getPath());
			}*/
			IDocumentService ds = ClientServiceHelper.getDocumentService();
			ByteArrayInputStream bis = null;
			for (int i = 0; i < ids.size(); i++) {
				bis = ds.getDocumentContent(ids.get(i));
				String path = MyApplication.SD_DIR + "/"
						+ filePath.substring(filePath.lastIndexOf("/") + 1);
				FileUtils.saveFile(bis, path);
			}

			Message msg = new Message();
			msg.arg1 = 2;
			ShareFileActivity.this.myHandler.sendMessage(msg);

		}

	}
}
