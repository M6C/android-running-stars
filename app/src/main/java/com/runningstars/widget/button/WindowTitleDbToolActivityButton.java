package com.runningstars.widget.button;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.runningstars.activity.DbToolActivity;

public class WindowTitleDbToolActivityButton extends Button {

	public WindowTitleDbToolActivityButton(Context context) {
		super(context);

      setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//
				Intent myIntent = new Intent(getContext(), DbToolActivity.class);

				getContext().startActivity(myIntent);
			}
		});
	}

}
