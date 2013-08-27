package com.runningstars.listener.view;

import android.view.View;

import com.cameleon.common.factory.FactoryDialog;
import com.runningstars.R;
import com.runningstars.activity.business.DbToolBusiness;
import com.runningstars.listener.onclick.ok.OnClickRestoreDbListenerOk;

public class ViewOnClickRestoreDbListener implements View.OnClickListener {
	 
	private DbToolBusiness business;

	public ViewOnClickRestoreDbListener(DbToolBusiness business) {
		super();
		this.business = business;
	}

	public void onClick(View view) {
		FactoryDialog.getInstance()
		.buildOkCancelDialog(business.getContext(), new OnClickRestoreDbListenerOk(business), R.string.dialog_restore_db_title, R.string.dialog_restore_db_message)
		.show();
    }
}
