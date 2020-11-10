package com.runningstars.listener.ontouch;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.listener.ongesture.AbstractGestureListener;
import com.runningstars.listener.ongesture.ParameterGestureListener;

public class ParameterOnTouchListener extends AbstractOnTouchListener {

	public ParameterOnTouchListener(AppCompatActivity parameterActivity) {
		super(parameterActivity);
	}

	@Override
	protected AbstractGestureListener getGestureListener(Activity context) {
		return new ParameterGestureListener((AppCompatActivity) context);
	}
}