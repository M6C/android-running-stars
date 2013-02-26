package com.runningstars.listener.onitemclick;

import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.runningstars.R;
import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.factory.FactoryDialog;
import com.runningstars.listener.onclick.ok.OnClickSessionDeleteListenerOk;
import com.runningstars.listener.onclick.ok.OnClickSessionKmlListenerOk;
import com.runningstars.listener.onclick.ok.OnClickSessionMapListenerOk;
import com.runningstars.listener.onclick.ok.OnClickSessionRepairListenerOk;
import com.runningstars.listener.onclick.ok.OnClickSessionSendListenerOk;

public class OnItemClickSessionDetailListener implements OnItemClickListener {

	private SessionDetailBusiness business;

	public OnItemClickSessionDetailListener(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		OnClickListener listener = null;
		int messageId = 0;
		switch (position) {
			case 0:
				listener = new OnClickSessionDeleteListenerOk(business);
				messageId = R.string.dialog_session_detail_delete;
				break;
	
			case 1:
				listener = new OnClickSessionRepairListenerOk(business);
				messageId = R.string.dialog_session_detail_repair;
				break;
				
			case 2:
				listener = new OnClickSessionSendListenerOk(business);
				messageId = R.string.dialog_session_detail_send;
				break;
				
			case 3:
				listener = new OnClickSessionKmlListenerOk(business);
				messageId = R.string.dialog_session_detail_kml;
				break;
				
			case 4:
				listener = new OnClickSessionMapListenerOk(business);
				messageId = R.string.dialog_session_detail_map;
				break;
		}
		FactoryDialog.getInstance()
		.buildOkCancelDialog(business.getContext(), listener, R.string.dialog_session_detail_title, messageId)
		.show();
	}

}
