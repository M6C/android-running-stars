package com.runningstars.listener.onclick.ok;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.runningstars.activity.ProfilActivity;
import com.runningstars.activity.SessionManagerActivity;
import com.runningstars.activity.business.SessionDetailBusiness;

public class OnClickSessionDeleteListenerOk implements OnClickListener {

	private SessionDetailBusiness business;

	public OnClickSessionDeleteListenerOk(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Delete session
        	business.delete();
        	// Back to the session manager
        	Context context = business.getContext();
//        	context.startActivity(new Intent(context, SessionManagerActivity.class));
        	context.startActivity(new Intent(context, ProfilActivity.class));
		}
	}

}
