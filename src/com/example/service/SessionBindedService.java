package com.example.service;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.example.service.binder.SessionBindedServiceBinder;

public class SessionBindedService extends IntentService {
	private static final String TAG = SessionBindedService.class.getCanonicalName();

	public static final String SERVICE_NAME = "SESSION_DETAIL_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	public SessionBindedService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	}

	/**
	 * Service Cycle life 
	 */

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		onStart(intent, -1);

		return new SessionBindedServiceBinder(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		logMe("Started");
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
