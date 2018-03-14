package com.runningstars.service.system.binder;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.runningstars.service.system.ISystemGpsLocationService;
import com.runningstars.service.system.SystemGpsLocationService;
import com.runningstars.service.system.receiver.SystemGpsSessionReceiver;

public class SystemGpsLocationServiceBinder extends ISystemGpsLocationService.Stub {

	private SystemGpsLocationService service;

	public SystemGpsLocationServiceBinder(SystemGpsLocationService service) {
		super();
		this.service = service;
	}

	public void startService(Session session) throws RemoteException {
    	Intent intent = new Intent(getContext(), SystemGpsLocationService.class);
    	intent.putExtra(SystemGpsSessionReceiver.INTENT_KEY_SESSION, session);
    	getContext().startService(intent);
	}

	public void stopService() throws RemoteException {
    	Intent intent = new Intent(getContext(), SystemGpsLocationService.class);
    	getContext().stopService(intent);
	}

	private Context getContext() {
		return service;
	}

	public boolean isListenerRegistred() throws RemoteException {
		return service.isListenerRegistred();
	}
}
