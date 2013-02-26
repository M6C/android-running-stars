package com.runningstars.service.system;

import org.gdocument.gtracergps.launcher.domain.Session;

interface ISystemGpsSessionService {
	void startService();
	void stopService();
	Session getSession();
}