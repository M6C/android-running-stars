package com.runningstars.tool;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

public class ToolLocationManager {

	private static ToolLocationManager instance;
	private static Context context;

	private static LocationManager locationManager;

	private ToolLocationManager() {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);   
	}

	public static ToolLocationManager getInstance(Context context) {
		ToolLocationManager.context = context;

		if (instance == null)
			instance = new ToolLocationManager();

		return instance;
	}

	public boolean isGpsEnabled() {
    	boolean gps_enabled = false;
    	try {
    		gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return gps_enabled;
    }

    public boolean isNetworkEnabled() {
		boolean network_enabled = false;
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return network_enabled;
    }
   
    public void requestGpsUpdates(LocationListener locationListener) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    public void removeUpdates(LocationListener locationListener) {
        locationManager.removeUpdates(locationListener);
    }
}
