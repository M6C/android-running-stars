package com.runningstars.listener.ongesture;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.activity.ApplicationDataPreferenceActivity;
import com.runningstars.activity.DbToolActivity;


public class SessionManagerGestureListener extends AbstractGestureListener {

	public SessionManagerGestureListener(AppCompatActivity context) {
		super(context);
	}

	@Override
	protected Intent getFlingRight(Context context) {
//		return new Intent(this.context.getBaseContext(), ParameterActivity.class);
		return new Intent(context, ApplicationDataPreferenceActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		return new Intent(context, DbToolActivity.class);
	}
}
