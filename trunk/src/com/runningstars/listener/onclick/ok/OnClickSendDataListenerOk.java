package com.runningstars.listener.onclick.ok;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.runningstars.activity.business.DbToolBusiness;

public class OnClickSendDataListenerOk implements OnClickListener {

	private DbToolBusiness business;

	public OnClickSendDataListenerOk(DbToolBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Send Session Not Send
        	business.sendSessionNotSend();
		}
	}

}
