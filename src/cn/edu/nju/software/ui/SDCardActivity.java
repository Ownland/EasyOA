package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.nju.software.file.FileOperator;
import cn.edu.nju.software.file.MIMEType;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SDCardActivity extends Activity {

	private SimpleAdapter adapter;
	private ListView sdfiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcard_activity);
		sdfiles = (ListView)findViewById(R.id.sd_files);
		fileList(0);
		
		sdfiles.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tv =(TextView)arg1.findViewById(R.id.SDfileName);
				String fileName = tv.getText().toString();
				String dir =MyApplication.SD_DIR;
				MIMEType.openFile(fileName, dir, SDCardActivity.this);
			}
			
		});
	}

	public void fileList(int id) {
		new AsyncTask<String, String, String>() {
			
			protected String doInBackground(String... params) {
				ArrayList<HashMap<String, Object>> data = FileOperator.sdFileList(SDCardActivity.this);
				adapter=new SimpleAdapter(SDCardActivity.this, data, R.layout.sdfile_list,
						new String[]{"fileIcon","fileName"}, 
						new int[]{R.id.SDimgFile,R.id.SDfileName});
				return null;
			}
			
			protected void onPostExecute(String result) {
				//设置ListActivity界面的方法 
				//同setContentView在Activity界面中的作用
				sdfiles.setAdapter(adapter);
				//列表更新动画
				Animation anim=AnimationUtils.loadAnimation(SDCardActivity.this, R.anim.zoomin);
				sdfiles.startAnimation(anim);
				super.onPostExecute(result);
			}
		}.execute();
	}
}
