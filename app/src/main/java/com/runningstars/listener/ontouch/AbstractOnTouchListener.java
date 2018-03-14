package com.runningstars.listener.ontouch;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.runningstars.application.GPSApplication;
import com.runningstars.listener.ongesture.AbstractGestureListener;

public abstract class AbstractOnTouchListener implements View.OnTouchListener {
//http://savagelook.com/blog/android/swipes-or-flings-for-navigation-in-android

	private GestureDetector gestureDetector = null;

	public AbstractOnTouchListener(Activity context) {
		gestureDetector = new GestureDetector(GPSApplication.getAppContext(), getGestureListener(context));
	}

	protected abstract AbstractGestureListener getGestureListener(Activity context);

	public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return false;
    }
}