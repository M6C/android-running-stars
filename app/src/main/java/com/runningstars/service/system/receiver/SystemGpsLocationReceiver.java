package com.runningstars.service.system.receiver;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsLocation;

public class SystemGpsLocationReceiver extends BroadcastReceiver {
	private static final String TAG = SystemGpsLocationReceiver.class.getCanonicalName();

	public static final String BROADCAST_ACTION = "BROADCAST_SYSTEM_GPS_LOCATION"; 
	public static final String INTENT_KEY_LOCALISATION = "INTENT_LOCATION"; 

	private INotifierSystemGpsLocation inotifierSystemGps;

	/**
	 * @param iSessionProgress
	 */
	public SystemGpsLocationReceiver(INotifierSystemGpsLocation inotifierSystemGps) {
		super();
		this.inotifierSystemGps = inotifierSystemGps;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		logMe("onReceive");
		Localisation localisation = intent.getParcelableExtra(INTENT_KEY_LOCALISATION);
    	logMe("onReceive localisation:"+localisation.toString());
		inotifierSystemGps.notifyLocation(localisation);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
