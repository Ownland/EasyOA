package cn.edu.nju.software.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.nju.software.model.Document;
import cn.edu.nju.software.ui.R;
import cn.edu.nju.software.ui.ShareFileActivity;

public class FileAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ArrayList<Document> filesData;

		public FileAdapter(Context context, ArrayList<Document> files) {
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

			ImageView fileImg = (ImageView) convertView
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
			fileOwner.setText(document.getUploaderId() + "");
			String time = document.getUploadDate().getYear() + "-"
					+ document.getUploadDate().getMonth() + "-"
					+ document.getUploadDate().getDay();
			fileDate.setText(time);
			int docId = document.getDocId();
			if (document.getType() == 0) {
				ckbFile.setVisibility(View.VISIBLE);
			} else
				ckbFile.setVisibility(View.GONE);
			ckbFile.setOnClickListener(new MyCheckboxListener(docId));

			return convertView;
		}
		
		class MyCheckboxListener implements OnClickListener {

			private int docid;

			public MyCheckboxListener(int id) {
				docid = id;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!ShareFileActivity.isChecked.containsKey(docid))
					ShareFileActivity.isChecked.put(docid, true);
				else if (ShareFileActivity.isChecked.get(docid)) {
					ShareFileActivity.isChecked.put(docid, false);
				} else
					ShareFileActivity.isChecked.put(docid, true);
			}

		}
	}