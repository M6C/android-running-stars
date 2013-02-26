package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.activity.SessionManagerActivity;
import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.SessionManagerGestureListener;

public class SessionManagerOnTouchListener extends AbstractOnTouchListener {

	public SessionManagerOnTouchListener(SessionManagerActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new SessionManagerGestureListener(context);
	}
}