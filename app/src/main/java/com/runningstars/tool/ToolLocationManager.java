package com.runningstars.tool;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

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
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
    		ex.printStackTrace();
    	}
    	return gps_enabled;
    }

    public boolean isNetworkEnabled() {
		boolean network_enabled = false;
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
		return network_enabled;
    }
   
    public void requestGpsUpdates(LocationListener locationListener) {
		try {
			// Premission Requiered - android.permission.ACCESS_FINE_LOCATION
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		} catch (Exception ex) {
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
    }
    
    public void removeUpdates(LocationListener locationListener) {
		try {
			locationManager.removeUpdates(locationListener);
		} catch (Exception ex) {
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
    }
}
