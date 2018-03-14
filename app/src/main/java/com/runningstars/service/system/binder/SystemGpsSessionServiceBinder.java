package com.runningstars.service.system.binder;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.runningstars.service.system.ISystemGpsSessionService;
import com.runningstars.service.system.SystemGpsSessionService;

public class SystemGpsSessionServiceBinder extends ISystemGpsSessionService.Stub {

	private SystemGpsSessionService service;

	public SystemGpsSessionServiceBinder(SystemGpsSessionService service) {
		super();
		this.service = service;
	}

	public void startService() throws RemoteException {
    	Intent intent = new Intent(getContext(), SystemGpsSessionService.class);
    	getContext().startService(intent);
	}

	public void stopService() throws RemoteException {
    	Intent intent = new Intent(getContext(), SystemGpsSessionService.class);
    	getContext().stopService(intent);
	}

	public Session getSession() throws RemoteException {
		return service.getSession();
	}
	
	private Context getContext() {
		return service;
	}
}
