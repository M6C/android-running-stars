package com.runningstars.service.map.receiver.inotifier;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;

public interface INotifierMapCalculateLocation {

	/**
	 * 
	 * @param list
	 */
	public void notifyLocation(List<Localisation> list);

	/**
	 * 
	 * @param localisation
	 * @param previous 
	 */
	public void notifyLocation(Localisation localisation, Localisation previous);
}
