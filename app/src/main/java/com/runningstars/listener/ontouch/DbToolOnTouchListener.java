package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.activity.DbToolActivity;
import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.DbToolGestureListener;

public class DbToolOnTouchListener extends AbstractOnTouchListener {

	public DbToolOnTouchListener(DbToolActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new DbToolGestureListener((DbToolActivity)context);
	}
}