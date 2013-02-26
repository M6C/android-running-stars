package com.runningstars.service.system.connection;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.runningstars.service.system.ISystemGpsSessionService;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsSessionServiceConnection;

public class SystemGpsSessionServiceConnection implements ServiceConnection {
	private static final String TAG = SystemGpsSessionServiceConnection.class.getCanonicalName();

	private INotifierSystemGpsSessionServiceConnection notifierServiceConnection; 

	public SystemGpsSessionServiceConnection(INotifierSystemGpsSessionServiceConnection notifierServiceConnection) {
		super();
		this.notifierServiceConnection = notifierServiceConnection;
	}

	public void onServiceConnected(ComponentName name, IBinder binder) {
		logMe("onServiceConnected");
		notifierServiceConnection.setSessionService(ISystemGpsSessionService.Stub.asInterface(binder));
	}

	public void onServiceDisconnected(ComponentName name) {
		logMe("onServiceDisconnected");
		notifierServiceConnection.setSessionService(null);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
