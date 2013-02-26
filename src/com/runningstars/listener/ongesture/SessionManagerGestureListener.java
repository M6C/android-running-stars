package com.runningstars.listener.ongesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.runningstars.activity.ApplicationDataPreferenceActivity;
import com.runningstars.activity.DbToolActivity;


public class SessionManagerGestureListener extends AbstractGestureListener {

	private Activity context;

	public SessionManagerGestureListener(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	protected Intent getFlingRight(Context context) {
//		return new Intent(this.context.getBaseContext(), ParameterActivity.class);
		return new Intent(this.context.getBaseContext(), ApplicationDataPreferenceActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		return new Intent(this.context.getBaseContext(), DbToolActivity.class);
	}
}
