package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.GPSLocationGestureListener;

public class GPSLocationOnTouchListener extends AbstractOnTouchListener {

	public GPSLocationOnTouchListener(GPSLocationActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new GPSLocationGestureListener((GPSLocationActivity)context);
	}
}