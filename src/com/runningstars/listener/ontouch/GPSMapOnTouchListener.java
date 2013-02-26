package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.activity.GPSMapActivity;
import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.GPSMapGestureListener;

public class GPSMapOnTouchListener extends AbstractOnTouchListener {

	public GPSMapOnTouchListener(GPSMapActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new GPSMapGestureListener((GPSMapActivity)context);
	}
}