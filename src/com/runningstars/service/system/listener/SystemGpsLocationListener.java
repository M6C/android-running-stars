package com.runningstars.service.system.listener;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;

import com.runningstars.service.system.SystemGpsLocationService;

public class SystemGpsLocationListener implements LocationListener {
	private static final String TAG = SystemGpsLocationListener.class.getCanonicalName();

	private Context context;

	public SystemGpsLocationListener(Context context) {
		super();
		this.context = context;
	}

	public void onLocationChanged(Location location) {
    	logMe("onLocationChanged");
		Localisation localisation = null;

    	if (location==null) {
			logEr("Location is null");
			return;
    	}

    	logMe("Location Changed (location: time:" + location.getTime() + " speed:" + location.getSpeed() + ")");// + " latitude:" + location.getLatitude() + " longitude:" + location.getLongitude() + " altitude:" + location.getAltitude() + " bearing:" + location.getBearing() + " accuracy:" + location.getAccuracy() + ")");

    	localisation = new Localisation();
		localisation.setSpeed(location.getSpeed());
		localisation.setLongitude(location.getLongitude());
		localisation.setLatitude(location.getLatitude());
		localisation.setAltitude(location.getAltitude());
		localisation.setAccuracy(location.getAccuracy());
		localisation.setBearing(location.getBearing());
		localisation.setTime((location.getTime()<=0) ? new Date().getTime() : location.getTime());

		try {
			Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
			List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses.size() > 0) {
				localisation.setAddressLine(addresses.get(0).getAddressLine(0));
				localisation.setPostalCode(addresses.get(0).getPostalCode());
				localisation.setLocality(addresses.get(0).getLocality());
			}
			else {
				localisation.setAddressLine("INDEFINI");
			}
		} catch (Exception e) {
			localisation.setAddressLine("INDEFINI ERREUR:" + e.getMessage());
			logMe(e);
		}

    	//Send Intent to Service
    	notifyService(localisation);
	}

	private Context getContext() {
		return context;
	}

	private void notifyService(Localisation localisation) {
    	logMe("notifyService");
    	logMe("notifyService localisation:"+localisation.toString());
		Intent intent = new Intent(getContext(), SystemGpsLocationService.class);
		intent.putExtra(SystemGpsLocationService.INTENT_KEY_LOCALISATION, (Parcelable)localisation);
		context.startService(intent);
	}

	public void onProviderDisabled(String provider)  {
    	logMe("onProviderDisabled");
    }    

    public void onProviderEnabled(String provider)  {
    	logMe("onProviderEnabled");
    }    

    public void onStatusChanged(String provider, int status, Bundle extras) {
    	logMe("onStatusChanged");
    }

	/**
     * Log Error methodes
     * @param msg
     */
    private void logEr(String msg) {
		Logger.logEr(TAG, msg);
    }

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }

	/**
     * Log methodes
     * @param ex
     */
	private void logMe(Exception ex) {
		Logger.logMe(TAG, ex);
    }
}
