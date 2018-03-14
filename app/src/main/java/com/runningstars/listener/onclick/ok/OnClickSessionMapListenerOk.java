package com.runningstars.listener.onclick.ok;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.runningstars.activity.business.SessionDetailBusiness;

public class OnClickSessionMapListenerOk implements OnClickListener {

	private SessionDetailBusiness business;

	public OnClickSessionMapListenerOk(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Send session
        	business.map();
		}
	}

}
