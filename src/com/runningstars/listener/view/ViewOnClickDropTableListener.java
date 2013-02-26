package com.runningstars.listener.view;

import android.view.View;

import com.runningstars.R;
import com.runningstars.activity.business.DbToolBusiness;
import com.runningstars.factory.FactoryDialog;
import com.runningstars.listener.onclick.ok.OnClickDropTableListenerOk;

public class ViewOnClickDropTableListener implements View.OnClickListener {
	 
	private DbToolBusiness business;

	public ViewOnClickDropTableListener(DbToolBusiness business) {
		super();
		this.business = business;
	}

	public void onClick(View view) {
		FactoryDialog.getInstance()
		.buildOkCancelDialog(business.getContext(), new OnClickDropTableListenerOk(business), R.string.dialog_drop_table_title, R.string.dialog_drop_table_message)
		.show();
    }
}
