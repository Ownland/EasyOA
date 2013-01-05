package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.nju.software.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PlazaActivity extends Activity {

	private GridView gridview;
	private int nowUserId;
	private Integer[] mThumbIds={//��ʾ��ͼƬ����
			  R.drawable.function1,R.drawable.function2,
			  R.drawable.function3,R.drawable.function4,
			  R.drawable.function5,R.drawable.function6,
			  R.drawable.function7,R.drawable.function8,
			  R.drawable.function9,R.drawable.function10,
			 };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plaza_activity);

		initView();
	}

	public void initView() {
		nowUserId = this.getIntent().getIntExtra("userId", -1);
		gridview = (GridView) findViewById(R.id.plazagridview);

		// ���ɶ�̬���飬����ת������
		String[] categoryArray = getResources().getStringArray(
				R.array.categories);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage",mThumbIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", categoryArray[i]);// �������ItemText
			lstImageItem.add(map);
		}
		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		SimpleAdapter saImageItems = new SimpleAdapter(this, // ûʲô����
				lstImageItem,// ������Դ
				R.layout.gridview_item,// night_item��XMLʵ��

				// ��̬������ImageItem��Ӧ������
				new String[] { "ItemImage", "ItemText" },

				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.ItemImage, R.id.ItemText });
		// ��Ӳ�����ʾ
		gridview.setAdapter(saImageItems);
		// �����Ϣ����
		gridview.setOnItemClickListener(new ItemClickListener());
	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// �ڱ�����arg2=arg3
			// HashMap<String, Object> item=(HashMap<String, Object>)
			// arg0.getItemAtPosition(arg2);
			// ��ʾ��ѡItem��ItemText
			// Toast.makeText(PlazaActivity.this, (String)item.get("ItemText"),
			// Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(PlazaActivity.this, ContactActivity.class);
			intent.putExtra("userId", nowUserId);
			startActivity(intent);
		}
	}
}
