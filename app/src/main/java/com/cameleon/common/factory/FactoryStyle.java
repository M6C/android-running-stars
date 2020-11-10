package com.cameleon.common.factory;

import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FactoryStyle {

	private static FactoryStyle instance;

	private FactoryStyle() {
	}

	public static FactoryStyle getInstance() {
		if (instance==null)
			instance = new FactoryStyle();
		return instance;
	}

	public void centerTitle(AppCompatActivity activity) {
		((TextView)activity.findViewById(android.R.id.title)).setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	}
}
