package com.example.service.binder;

import android.os.Binder;

import com.example.service.SessionBindedService;

public class SessionBindedServiceBinder extends Binder {

	private SessionBindedService service;
	
	public SessionBindedServiceBinder(SessionBindedService service) {
		this.service = service;
	}

	public SessionBindedService getService() {
		return service;
	}
}
