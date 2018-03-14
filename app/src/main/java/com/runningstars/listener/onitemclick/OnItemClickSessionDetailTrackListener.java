package com.runningstars.listener.onitemclick;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.runningstars.activity.business.SessionDetailBusiness;

public class OnItemClickSessionDetailTrackListener implements OnItemClickListener {

	private SessionDetailBusiness business;

	public OnItemClickSessionDetailTrackListener(SessionDetailBusiness business) {
		this.business = business;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		OnClickListener listener = null;
//		int messageId = 0;
		switch (position) {				
			case 0:
//				listener = new OnClickSessionKmlListenerOk(business);
//				messageId = R.string.dialog_session_detail_kml;
				business.kml();
				break;
				
			case 1:
//				listener = new OnClickSessionMapListenerOk(business);
//				messageId = R.string.dialog_session_detail_map;
				business.map();
				break;
		}
//		FactoryDialog.getInstance()
//		.buildOkCancelDialog(business.getContext(), listener, R.string.dialog_session_detail_title, messageId)
//		.show();
	}

}
