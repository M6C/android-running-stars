package com.runningstars.activity.adapter;

import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runningstars.R;
import com.runningstars.activity.ProfilActivity;
import com.runningstars.tool.ToolCalculate;
import com.runningstars.tool.ToolDatetime;

public class ProfilSessionAdapter extends ArrayAdapter<Session> {

	private List<Session> value;
	private ProfilActivity activity;
	
//	private SessionBusiness sessionBusiness;

	//http://www.vogella.com/articles/AndroidListView/article.html
	public ProfilSessionAdapter(ProfilActivity activity, int resource, int textViewResourceId, List<Session> value) {
		super(activity, resource, textViewResourceId, value);

		this.activity = activity;
		this.value = value;

//		sessionBusiness = SessionBusiness.getInstance(getContext(), activity, activity.getHandler());
	}


	static class ViewHolder {
		public ImageView imageMap;
		public TextView date;
		public TextView distance;
		public TextView elapsedTime;
		public TextView speed;
		public ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder viewHolder;
		final Session s = value.get(position);
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.activity_session_rowlayout, null);

			viewHolder = new ViewHolder();
			viewHolder.imageMap = (ImageView) rowView.findViewById(R.id.ImageMap);
			viewHolder.date = (TextView) rowView.findViewById(R.id.TextViewDate);
			viewHolder.distance = (TextView) rowView.findViewById(R.id.TextViewDistance);
			viewHolder.elapsedTime = (TextView) rowView.findViewById(R.id.TextViewElapsedTime);
			viewHolder.speed = (TextView) rowView.findViewById(R.id.TextViewSpeed);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.ImageView01);

			rowView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)rowView.getTag();
		}

// TODO Mise en commentaire à cause d'une exception OutOfMemory au 2eme chargement des images
//		    try {
//		    	byte[] buf = s.getPngMapScreenShoot();
//		    	if (buf==null) {
//		    		buf = sessionBusiness.getPngMapScreenShoot(s);
//		    		s.setPngMapScreenShoot(buf);
//		    	}
//		    	if (buf!=null) {
//		    		ByteArrayInputStream bais = null;
//		    		try {
//			    		bais = new ByteArrayInputStream(buf);
//			    		viewHolder.imageMap.setImageBitmap(BitmapFactory.decodeStream(bais));
//		    		}
//		    		finally {
//		    			if (bais!=null)
//		    				bais.close();
//		    		}
//		    	}
//		    	else {
//			    	viewHolder.imageMap.setImageResource(R.drawable.no_image);
//		    	}
//		    }
//		    catch(Exception ex) {
//		    	viewHolder.imageMap.setImageResource(R.drawable.no_image);
//		    }

		long elapsedTime = new Date().getTime() - s.getTimeStart().getTime();

		viewHolder.date.setTextColor(getDateTextColorByDay(elapsedTime));
		viewHolder.date.setText(s.getTimeStart() == null ? "" : ToolDatetime.formatToNbSecMinHourDay(elapsedTime));
		viewHolder.distance.setText(ToolCalculate.formatDistanceKm(s));
		viewHolder.elapsedTime.setText(ToolCalculate.formatElapsedTime(s));
		viewHolder.speed.setText(ToolCalculate.formatSpeedKmH(s));
		if (s.getTimeSend() == null) {
			viewHolder.image.setImageResource(R.drawable.session_to_send_04);
		} else {
			viewHolder.image.setImageResource(R.drawable.session_send_04);
		}

		    return rowView;
	}

	/**
	 * Format a String representing time in nb Sec, Min, Hour and Day
	 * @param time to format
	 * @return String of nb Sec, Min, Hour and Day
	 */
	private int getDateTextColorByDay(long time) {
		int id = R.color.yellow_golden;

		long nbSec = 1000;
		long nbMin = 60 * nbSec;
		long nbHou = 60 * nbMin;
		long nbDay = 24 * nbHou;

		if (time > nbDay) {

			long nb = time / nbDay;
			if (nb > 60) {
				id = R.color.orange_mahohany;
			}
			else if (nb > 30) {
				id = R.color.orange_tangelo;
			}
			else if (nb > 20) {
				id = R.color.orange_rich;
			}
			else if (nb > 10) {
				id = R.color.orange_pumpkin;
			}
			else if (nb > 7) {
				id = R.color.orange_bright;
			}
			else {
				id = R.color.orange;
			}
		}

		return activity.getResources().getColor(id);
	}

	/**
	 * @return the value
	 */
	public List<Session> getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(List<Session> value) {
		this.value = value;
	}
}
