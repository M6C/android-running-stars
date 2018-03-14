package com.runningstars.listener.onclick.ok;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.runningstars.activity.business.SessionDetailBusiness;

public class OnClickSessionRepairListenerOk implements OnClickListener {

	private SessionDetailBusiness business;

	public OnClickSessionRepairListenerOk(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Repaire session
        	business.repaire();
		}
	}

}
