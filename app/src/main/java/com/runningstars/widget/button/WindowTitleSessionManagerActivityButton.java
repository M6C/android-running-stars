package com.runningstars.widget.button;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.runningstars.activity.ProfilActivity;

public class WindowTitleSessionManagerActivityButton extends AppCompatButton {

	public WindowTitleSessionManagerActivityButton(Context context) {
		super(context);

      setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//
//				Intent myIntent = new Intent(getContext(), SessionManagerActivity.class);
				Intent myIntent = new Intent(getContext(), ProfilActivity.class);

				getContext().startActivity(myIntent);
			}
		});
	}

}
