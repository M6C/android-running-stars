package com.runningstars.service.system.receiver;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsSession;

public class SystemGpsSessionReceiver extends BroadcastReceiver {
	private static final String TAG = SystemGpsSessionReceiver.class.getCanonicalName();

	public static final String BROADCAST_ACTION = "BROADCAST_SYSTEM_GPS_SESSION"; 
	public static final String INTENT_KEY_SESSION = "INTENT_SESSION"; 

	private INotifierSystemGpsSession inotifierSystemGps;

	/**
	 * @param iSessionProgress
	 */
	public SystemGpsSessionReceiver(INotifierSystemGpsSession inotifierSystemGps) {
		super();
		this.inotifierSystemGps = inotifierSystemGps;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		logMe("onReceive");
		Session session = intent.getParcelableExtra(INTENT_KEY_SESSION);
    	logMe("onReceive session:"+(session==null ? "null" : session.toString()));
		inotifierSystemGps.notifySession(session);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
