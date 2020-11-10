package com.runningstars.listener.ongesture;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.activity.DbToolActivity;
import com.runningstars.activity.ProfilActivity;


public class ParameterGestureListener extends AbstractGestureListener {

	public ParameterGestureListener(AppCompatActivity context) {
		super(context);
	}

	@Override
	protected Intent getFlingRight(Context context) {
		return new Intent(context, DbToolActivity.class);
	}

	@Override
	protected Intent getFlingLeft(Context context) {
//		return new Intent(this.context.getBaseContext(), SessionManagerActivity.class);
		return new Intent(context, ProfilActivity.class);
	}
}
