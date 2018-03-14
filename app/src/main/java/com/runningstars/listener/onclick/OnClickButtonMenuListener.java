package com.runningstars.listener.onclick;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.activity.ProfilActivity;

public class OnClickButtonMenuListener implements OnClickListener {

	private GPSLocationActivity context;

	public OnClickButtonMenuListener(GPSLocationActivity context) {
		this.context = context;
	}

	public void onClick(View v) {
//		context.startActivity(new Intent(context, SessionManagerActivity.class));
		context.startActivity(new Intent(context, ProfilActivity.class));
	}

}
