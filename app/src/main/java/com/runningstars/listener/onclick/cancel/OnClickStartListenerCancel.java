package com.runningstars.listener.onclick.cancel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class OnClickStartListenerCancel implements OnClickListener {

	private Context context;

	public OnClickStartListenerCancel(Context context) {
		this.context = context;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_NEUTRAL) {
			String msg = "location is not getting due to location provider";
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

}
