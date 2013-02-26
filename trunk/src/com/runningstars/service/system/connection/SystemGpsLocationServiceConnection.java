package com.runningstars.service.system.connection;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.runningstars.service.system.ISystemGpsLocationService;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsLocationServiceConnection;

public class SystemGpsLocationServiceConnection implements ServiceConnection {
	private static final String TAG = SystemGpsLocationServiceConnection.class.getCanonicalName();

	private INotifierSystemGpsLocationServiceConnection notifierServiceConnection; 

	public SystemGpsLocationServiceConnection(INotifierSystemGpsLocationServiceConnection notifierServiceConnection) {
		super();
		this.notifierServiceConnection = notifierServiceConnection;
	}

	public void onServiceConnected(ComponentName name, IBinder binder) {
		logMe("onServiceConnected");
		notifierServiceConnection.setLocationService(ISystemGpsLocationService.Stub.asInterface(binder));
	}

	public void onServiceDisconnected(ComponentName name) {
		logMe("onServiceDisconnected");
		notifierServiceConnection.setLocationService(null);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
