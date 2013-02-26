package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.activity.SessionDetailActivity;
import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.SessionDetailGestureListener;

public class SessionDetailOnTouchListener extends AbstractOnTouchListener {

	public SessionDetailOnTouchListener(SessionDetailActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new SessionDetailGestureListener((SessionDetailActivity)context);
	}
}