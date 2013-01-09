package cn.edu.nju.software.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.ui.R;


public class ListViewEmailAdapter extends BaseAdapter {
	private Context 					context;
	private List<Email> 					listItems;
	private LayoutInflater 				listContainer;
	private int 						itemViewResource;
	static class ListItemView{				 
	        public TextView title;  
		    public TextView sender;
		    public TextView date;  
	 }  


	public ListViewEmailAdapter(Context context, List<Email> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			listItemView.title = (TextView)convertView.findViewById(R.id.email_listitem_title);
			listItemView.sender = (TextView)convertView.findViewById(R.id.email_listitem_sender);
			listItemView.date= (TextView)convertView.findViewById(R.id.email_listitem_date);
			
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		Email email = listItems.get(position);
		if(email!=null){
			EmailType type= email.getType();
			
			listItemView.title.setText(email.getTitle());
			listItemView.title.setTag(email);
			if(type==EmailType.INBOXMAIL){
				listItemView.sender.setText(email.getSender());
			}else{
				listItemView.sender.setText(email.getReciever());
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			listItemView.date.setText(df.format(email.getDate()));
		}
		return convertView;
	}
}