package com.runningstars.listener.onclick;

import android.view.View;
import android.view.View.OnClickListener;

import com.runningstars.activity.GPSLocationActivity;

public class OnClickButtonRunListener implements OnClickListener {

	private GPSLocationActivity context;

	public OnClickButtonRunListener(GPSLocationActivity context) {
		this.context = context;
	}

	public void onClick(View v) {
		context.setRun(!context.isRun());
		if (context.isRun()) {
			context.getBusiness().startListening();
//			((ImageView)v).setImageResource(R.drawable.title_red_button_run);
		}
		else {
			context.getBusiness().stopListening();
//			((ImageView)v).setImageResource(R.drawable.title_red_button_no_run);
		}
	}

}
