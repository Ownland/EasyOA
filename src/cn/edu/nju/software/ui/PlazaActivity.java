package cn.edu.nju.software.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.nju.software.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PlazaActivity extends Activity {

	private long waitTime = 2000;
	private long touchTime = 0;

	private GridView gridview;
	private Integer[] mThumbIds = {// 显示的图片数组
	R.drawable.function1, R.drawable.function2, R.drawable.function3,
			R.drawable.function4, R.drawable.function5, R.drawable.function6,
			R.drawable.function7, R.drawable.function8, R.drawable.function9,
			R.drawable.function10, };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plaza_activity);
		initView();
	}

	public void initView() {
		gridview = (GridView) findViewById(R.id.plazagridview);

		// 生成动态数组，并且转入数据
		String[] categoryArray = getResources().getStringArray(
				R.array.categories);

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mThumbIds[i]);// 添加图像资源的ID
			map.put("ItemText", categoryArray[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}

		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,
				R.layout.gridview_item,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
		
		makeDir();
	}

	
	public void makeDir(){
		String mydir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EasyOA";
		File destDir = new File(mydir);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			switch (arg2) {
			case 0:
				intent.setClass(PlazaActivity.this, ContactActivity.class);
				break;
			case 1:
				intent.setClass(PlazaActivity.this, EmailActivity.class);
				break;
			case 2:
				intent.setClass(PlazaActivity.this, CalendarActivity.class);
				break;
			case 3:
				intent.setClass(PlazaActivity.this, ShareFileActivity.class);
				break;
			default:
				
				;
			}
			startActivity(intent);
		}
	}
}
