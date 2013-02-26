package com.runningstars.service.map.receiver;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.runningstars.service.map.receiver.inotifier.INotifierMapCalculateLocation;

public class MapCalculateLocationReceiver extends BroadcastReceiver {

	public static final String BROADCAST_ACTION = "BROADCAST_MAP_CALCULATE_LOCATION"; 
	public static final String INTENT_KEY_LIST_LOCATION = "INTENT_KEY_LIST_LOCATION"; 
	public static final String INTENT_KEY_LOCATION = "INTENT_KEY_LOCATION"; 
	public static final String INTENT_KEY_LOCATION_PREVIOUS = "INTENT_KEY_LOCATION_PREVIOUS";

	private INotifierMapCalculateLocation iNotifier;

	/**
	 * @param iSessionProgress
	 */
	public MapCalculateLocationReceiver(INotifierMapCalculateLocation iNotifier) {
		super();
		this.iNotifier = iNotifier;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(INTENT_KEY_LIST_LOCATION)) {
			List<Localisation> list = intent.getParcelableArrayListExtra(INTENT_KEY_LIST_LOCATION);
			iNotifier.notifyLocation(list);
		}
		else if (intent.hasExtra(INTENT_KEY_LOCATION)) {
			Localisation localisation = intent.getParcelableExtra(INTENT_KEY_LOCATION);
			Localisation previous = intent.getParcelableExtra(INTENT_KEY_LOCATION_PREVIOUS);
			iNotifier.notifyLocation(localisation, previous);
		}
	}

}
