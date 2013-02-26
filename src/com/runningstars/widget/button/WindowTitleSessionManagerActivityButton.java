package com.runningstars.widget.button;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.runningstars.activity.SessionManagerActivity;

public class WindowTitleSessionManagerActivityButton extends Button {

	public WindowTitleSessionManagerActivityButton(Context context) {
		super(context);

      setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//
				Intent myIntent = new Intent(getContext(), SessionManagerActivity.class);

				getContext().startActivity(myIntent);
			}
		});
	}

}
