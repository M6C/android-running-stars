package com.runningstars.listener.ongesture;

import android.content.Context;
import android.content.Intent;

import com.runningstars.ApplicationData;
import com.runningstars.activity.ApplicationDataPreferenceActivity;
import com.runningstars.activity.DbToolActivity;
import com.runningstars.activity.ProfilActivity;


public class DbToolGestureListener extends AbstractGestureListener {

	private DbToolActivity context;

	public DbToolGestureListener(DbToolActivity context) {
		super(context);
		this.context = context;
	}

	@Override
	protected Intent getFlingRight(Context context) {
		// Save Url in applcation data
		ApplicationData.getInstance(context).setMysqlServerUrl(this.context.getUrl());

//		return new Intent(this.context.getBaseContext(), SessionManagerActivity.class);
		return new Intent(this.context.getBaseContext(), ProfilActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		// Save Url in applcation data
		ApplicationData.getInstance(context).setMysqlServerUrl(this.context.getUrl());

//		return new Intent(this.context.getBaseContext(), ParameterActivity.class);
		return new Intent(this.context.getBaseContext(), ApplicationDataPreferenceActivity.class);
	}
}
