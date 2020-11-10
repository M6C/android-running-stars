package com.runningstars.listener.ontouch;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.SessionManagerGestureListener;

public class SessionManagerOnTouchListener extends AbstractOnTouchListener {

	public SessionManagerOnTouchListener(AppCompatActivity context) {
		super(context);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new SessionManagerGestureListener((AppCompatActivity)context);
	}
}