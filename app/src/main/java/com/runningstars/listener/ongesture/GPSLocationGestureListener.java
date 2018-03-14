package com.runningstars.listener.ongesture;

import android.content.Context;
import android.content.Intent;

import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.activity.GPSMapActivity;


public class GPSLocationGestureListener extends AbstractGestureListener {

	public GPSLocationGestureListener(GPSLocationActivity context) {
		super(context);
	}

	@Override
	protected Intent getFlingRight(Context context) {
		return new Intent(context, GPSMapActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		return new Intent(context, GPSMapActivity.class);
	}
}
