package com.runningstars.listener.onclick.interrupter;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.activity.business.GPSLocationBusiness;

public class OnClickSwitchListener implements OnCheckedChangeListener {

	private GPSLocationBusiness business;
	private GPSLocationActivity context;

	public OnClickSwitchListener(GPSLocationBusiness business) {
		this.business = business;
		this.context = (GPSLocationActivity)business.getContext();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			start(buttonView);
		}
		else {
			stop(buttonView);
		}
	}

	private void start(CompoundButton buttonView) {
		business.startListening();
	}

	private void stop(CompoundButton buttonView) {
		business.stopListening();
	}
}
