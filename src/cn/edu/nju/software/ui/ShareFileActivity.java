package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




import cn.edu.nju.software.enums.DocumentType;
import cn.edu.nju.software.file.FileOperator;
import cn.edu.nju.software.model.Document;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class ShareFileActivity extends Activity {

	private Context context;
	private ListView lstFiles;
	private ArrayList<Document> files;
	private RelativeLayout progress;
	private FileAdapter adapter;
	private String filePath = ".";
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);
		
		init();
		
		lstFiles.setOnItemClickListener(new FileItemClickListener());
	}
	
	public void init(){
		context = this;
		lstFiles = (ListView) findViewById(R.id.Lsv_files);
		lstFiles.setVisibility(View.VISIBLE);
		progress = (RelativeLayout)findViewById(R.id.rl_filebar);
		files = new ArrayList<Document>();
		Date date = new Date();
		Document dir1 = new Document(1,"folder1","docx",1,date,-1,DocumentType.FOLDER,0);
		Document dir2 = new Document(2,"folder2","xlsx",1,date,-1,DocumentType.FOLDER,0);
		Document dir3 = new Document(3,"folder3","xlsx",1,date,1,DocumentType.FOLDER,0);
		Document dir4 = new Document(12,"folder4","xlsx",1,date,-1,DocumentType.FOLDER,0);
		Document file1 = new Document(4,"file1","xlsx",1,date,-1,DocumentType.DOC,0);
		Document file2 = new Document(5,"file2","xlsx",1,date,-1,DocumentType.DOC,0);
		Document file3 = new Document(6,"file3","xlsx",1,date,1,DocumentType.DOC,0);
		Document file4 = new Document(7,"file4","xlsx",1,date,1,DocumentType.DOC,0);
		Document file5 = new Document(8,"file5","xlsx",1,date,3,DocumentType.DOC,0);
		Document file6 = new Document(9,"file6","xlsx",1,date,3,DocumentType.DOC,0);
		Document file7 = new Document(10,"file1","xlsx",1,date,2,DocumentType.DOC,0);
		Document file8 = new Document(11,"file1","xlsx",1,date,2,DocumentType.DOC,0);
		
		files.add(file5);files.add(file6);files.add(file7);files.add(file8);
		files.add(file1);files.add(file2);files.add(file3);files.add(file4);
		files.add(dir1);files.add(dir2);files.add(dir3);files.add(dir4);
		fileList(-1);
	}
	public void fileList(final int id){
		//progress.setVisibility(View.VISIBLE);
		new AsyncTask<String,String,String>(){
			protected String doInBackground(String... params) {
				ArrayList<Document> data = FileOperator.fileList(files,id);
				adapter = new FileAdapter(context,data);
				
				return null;
			}
			protected void onPostExecute(String result){
				//progress.setVisibility(View.GONE);
				lstFiles.setAdapter(adapter);
				Animation anim=AnimationUtils.loadAnimation(ShareFileActivity.this, R.anim.zoomin);
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
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	class FileAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private ArrayList<Document> filesData;
		public FileAdapter(Context context, ArrayList<Document> files){
			inflater = LayoutInflater.from(context);
			filesData = files;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return filesData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return filesData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return filesData.get(position).getDocId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = inflater.inflate(R.layout.file_list, null);
			
			ImageView fileImg = (ImageView)convertView
					.findViewById(R.id.imgFile);
			TextView fileName = (TextView) convertView
					.findViewById(R.id.fileName);
			TextView fileOwner = (TextView) convertView
					.findViewById(R.id.fileUploader);
			TextView fileDate = (TextView) convertView
					.findViewById(R.id.fileDate);
			CheckBox ckbFile = (CheckBox) convertView
					.findViewById(R.id.ckbFile);
			Document document = filesData.get(position);
			
			
			fileImg.setBackgroundResource(document.getResource());

			fileName.setText(document.getTitle());
			fileOwner.setText(document.getUploaderId()+"");
			String time = document.getUploadDate().getYear()+"-" + document.getUploadDate().getMonth() + "-" + document.getUploadDate().getDay();
			fileDate.setText(time);
			if(document.getType().value() == 1){
				ckbFile.setVisibility(View.VISIBLE);
			}
			else
				ckbFile.setVisibility(View.GONE);
			return convertView;
		}
	}
	
	
	class FileItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Document cur = (Document)arg0.getItemAtPosition(arg2);
			if(cur.getType().value() == 1){
				Toast.makeText(ShareFileActivity.this, "´ò¿ª"+cur.getTitle(), Toast.LENGTH_SHORT).show();
			}
			else{
				fileList(cur.getDocId());
			}
		}
		
	}
}
