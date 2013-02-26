package com.runningstars.service.system.receiver.inotifier;

import org.gdocument.gtracergps.launcher.domain.Localisation;

public interface INotifierSystemGpsLocation {

	/**
	 * 
	 * @param localisation
	 */
	public void notifyLocation(Localisation localisation);
}
