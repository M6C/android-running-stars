package com.runningstars.listener.onclick.ok;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.runningstars.activity.business.DbToolBusiness;

public class OnClickBackupDbListenerOk implements OnClickListener {

	private DbToolBusiness business;

	public OnClickBackupDbListenerOk(DbToolBusiness business) {
		this.business = business;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
            // Database backup
        	business.dbBackup();
		}
	}

}
