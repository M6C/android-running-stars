package com.runningstars.listener.view;

import android.view.View;

import com.cameleon.common.factory.FactoryDialog;
import com.runningstars.R;
import com.runningstars.activity.business.DbToolBusiness;
import com.runningstars.listener.onclick.ok.OnClickSendDataListenerOk;

public class ViewOnClickSendDataListener implements View.OnClickListener {
	 
	private DbToolBusiness business;

	public ViewOnClickSendDataListener(DbToolBusiness business) {
		super();
		this.business = business;
	}

	public void onClick(View view) {
		FactoryDialog.getInstance()
		.buildOkCancelDialog(business.getContext(), new OnClickSendDataListenerOk(business), R.string.dialog_send_data_title, R.string.dialog_send_data_message)
		.show();
    }
}
