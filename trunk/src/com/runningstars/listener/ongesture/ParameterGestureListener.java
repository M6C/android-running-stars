package com.runningstars.listener.ongesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.runningstars.activity.DbToolActivity;
import com.runningstars.activity.SessionManagerActivity;


public class ParameterGestureListener extends AbstractGestureListener {

	private Activity context;

	public ParameterGestureListener(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	protected Intent getFlingRight(Context context) {
		return new Intent(this.context.getBaseContext(), DbToolActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		return new Intent(this.context.getBaseContext(), SessionManagerActivity.class);
	}
}
