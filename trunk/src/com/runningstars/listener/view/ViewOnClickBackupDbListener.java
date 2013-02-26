package com.runningstars.listener.view;

import android.view.View;

import com.runningstars.R;
import com.runningstars.activity.business.DbToolBusiness;
import com.runningstars.factory.FactoryDialog;
import com.runningstars.listener.onclick.ok.OnClickBackupDbListenerOk;

public class ViewOnClickBackupDbListener implements View.OnClickListener {
	 
	private DbToolBusiness business;

	public ViewOnClickBackupDbListener(DbToolBusiness business) {
		super();
		this.business = business;
	}

	public void onClick(View view) {
		FactoryDialog.getInstance()
		.buildOkCancelDialog(business.getContext(), new OnClickBackupDbListenerOk(business), R.string.dialog_backup_db_title, R.string.dialog_backup_db_title)
		.show();
    }
}
