package com.runningstars.listener.onclick.ok;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.runningstars.activity.business.SessionDetailBusiness;

public class OnClickSessionSendListenerOk implements OnClickListener {

	private SessionDetailBusiness business;

	public OnClickSessionSendListenerOk(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Send session
        	business.send();
		}
	}

}
