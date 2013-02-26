package com.runningstars.service.system.receiver.inotifier;

import org.gdocument.gtracergps.launcher.domain.Session;

public interface INotifierSystemGpsSession {

	/**
	 * 
	 * @param localisation
	 */
	public void notifySession(Session session);
}
