package cn.edu.nju.software.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.ui.R;
import cn.edu.nju.software.utils.DateUtil;


public class ListViewCalendarDoneAdapter extends BaseAdapter {
	private Context 					context;
	private List<Calendarevent> 					listItems;
	private LayoutInflater 				listContainer;
	private int 						itemViewResource;
	static class ListItemView{				 
	        public TextView title;  
		    public TextView weekday;
		    public TextView date;  
	 }  


	public ListViewCalendarDoneAdapter(Context context, List<Calendarevent> data,int resource) {
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
			listItemView.title = (TextView)convertView.findViewById(R.id.calendar_title_done);
			listItemView.weekday = (TextView)convertView.findViewById(R.id.calendar_weekday_done);
			listItemView.date= (TextView)convertView.findViewById(R.id.calendar_date_done);
			
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		Calendarevent event = listItems.get(position);
		if(event!=null){
			listItemView.title.setText(event.getName());
			listItemView.title.setTag(event);
			listItemView.weekday.setText(new DateUtil().getWeekdayByDate(event.getBeginTime()));
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			listItemView.date.setText(df.format(event.getBeginTime()));
		}
		return convertView;
	}
}