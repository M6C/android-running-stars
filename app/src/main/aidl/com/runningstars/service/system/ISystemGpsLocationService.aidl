package com.runningstars.service.system;

import org.gdocument.gtracergps.launcher.domain.Session;

interface ISystemGpsLocationService {
	void startService(in Session session);
	void stopService();
	boolean isListenerRegistred();
}