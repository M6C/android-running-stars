package com.example.service.connection;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.service.binder.SessionBindedServiceBinder;

public class SessionBindedServiceConnection implements ServiceConnection {
	private static final String TAG = SessionBindedServiceConnection.class.getCanonicalName();

	private ISessionBindedServiceConnection sessionDetailServiceConnection; 

	public SessionBindedServiceConnection(ISessionBindedServiceConnection sessionDetailServiceConnection) {
		super();
		this.sessionDetailServiceConnection = sessionDetailServiceConnection;
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		logMe("onServiceConnected");
		sessionDetailServiceConnection.setSessionDetailService(((SessionBindedServiceBinder)service).getService());
	}

	public void onServiceDisconnected(ComponentName name) {
		logMe("onServiceDisconnected");
		sessionDetailServiceConnection.setSessionDetailService(null);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
