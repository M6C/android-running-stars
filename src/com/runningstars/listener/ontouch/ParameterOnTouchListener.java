package com.runningstars.listener.ontouch;

import android.app.Activity;

import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.ParameterGestureListener;

public class ParameterOnTouchListener extends AbstractOnTouchListener {

	public ParameterOnTouchListener(Activity parameterActivity) {
		super(parameterActivity);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new ParameterGestureListener((Activity)context);
	}
}