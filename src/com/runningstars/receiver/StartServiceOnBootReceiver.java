package com.runningstars.receiver;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.runningstars.service.system.SystemGpsLocationService;

public class StartServiceOnBootReceiver extends BroadcastReceiver {
	private static final String TAG = StartServiceOnBootReceiver.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent) {
		logMe("onReceive");
        startService(context);
	}

	private void startService(Context context) {
		logMe("startService");
        Intent startServiceIntent = new Intent(context, SystemGpsLocationService.class);
        context.startService(startServiceIntent);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
