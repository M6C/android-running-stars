package com.runningstars.listener.ongesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.activity.GPSMapActivity;


public class GPSLocationGestureListener extends AbstractGestureListener {

	private Activity context;

	public GPSLocationGestureListener(GPSLocationActivity context) {
		super(context);
		this.context = context;
	}

	@Override
	protected Intent getFlingRight(Context context) {
		return new Intent(this.context.getBaseContext(), GPSMapActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		return new Intent(this.context.getBaseContext(), GPSMapActivity.class);
	}
}
