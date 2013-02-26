package com.runningstars.activity.adapter;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SessionDetailAdapter extends ArrayAdapter<Localisation> {

	private List<Localisation> value;
	private Activity activity;
	
	//http://www.vogella.com/articles/AndroidListView/article.html
	public SessionDetailAdapter(Activity activity, int resource, int textViewResourceId, List<Localisation> value) {
		super(activity, resource, textViewResourceId, value);

		this.activity = activity;
		this.value = value;
	}


	static class ViewHolder {
		public TextView textId;
		public TextView text;
		public ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		   View rowView = convertView;
//		    if (rowView == null) {
//		      LayoutInflater inflater = activity.getLayoutInflater();
//		      rowView = inflater.inflate(R.layout.activity_session_rowlayout, null);
//		      ViewHolder viewHolder = new ViewHolder();
//		      viewHolder.textId = (TextView) rowView.findViewById(R.id.TextViewId);
//		      viewHolder.text = (TextView) rowView.findViewById(R.id.TextView01);
//		      viewHolder.image = (ImageView) rowView.findViewById(R.id.ImageView01);
//		      rowView.setTag(viewHolder);
//		    }
//
//		    ViewHolder holder = (ViewHolder) rowView.getTag();
//		    Location s = value.get(position);
//		    holder.textId.setText(Long.toString(s.getId()));
//		    holder.text.setText(df.format(s.getTimeStart()));
//		    if (s.getTimeSend()==null) {
//		    	holder.image.setImageResource(R.drawable.ko);
//		    } else {
//		    	holder.image.setImageResource(R.drawable.ok);
//		    }

		    return rowView;
	}

}
