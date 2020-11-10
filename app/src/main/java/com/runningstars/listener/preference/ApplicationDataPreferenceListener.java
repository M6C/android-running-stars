package com.runningstars.listener.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.runningstars.ApplicationData;
import com.runningstars.activity.IPreference;

import org.gdocument.gtracergps.GpsConstant;
import org.gdocument.gtracergps.launcher.log.Logger;

public class ApplicationDataPreferenceListener implements SharedPreferences.OnSharedPreferenceChangeListener {

	private Context activity;
	private IPreference preference;
	
	public ApplicationDataPreferenceListener(Context activity, IPreference preference) {
		this.activity = activity;
		this.preference = preference;
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if ("perform_log".equals(key)) {
        	GpsConstant.LOG_WRITE = sharedPreferences.getBoolean(key, false);
        	if (GpsConstant.LOG_WRITE && GpsConstant.LOG_WRITE_SD) {
				Logger.initLogSD();
        	}
        }
        else if ("perform_log_sysout".equals(key)) {
			GpsConstant.LOG_WRITE_SYSOUT = sharedPreferences.getBoolean(key, false);
        }
        else if ("perform_log_sd".equals(key)) {
        	GpsConstant.LOG_WRITE_SD = sharedPreferences.getBoolean(key, false);
        	if (GpsConstant.LOG_WRITE_SD) {
				Logger.initLogSD();
        	}
        }
        else if ("screenshoot_quality".equals(key)) {
        	int progress = Integer.parseInt(sharedPreferences.getString(key, "0"));
			ApplicationData.getInstance(activity).setMapScreenShootQuality(progress);
        }
        else if ("map_zoom".equals(key)) {
        	int progress = Integer.parseInt(sharedPreferences.getString(key, "0"));
			ApplicationData.getInstance(activity).setGpsLocationMapZoom(progress);
        }
        else if ("map_scale".equals(key)) {
        	int scale = Integer.parseInt(sharedPreferences.getString(key, "0"));
			ApplicationData.getInstance(activity).setMapScaleInMeter(scale);
        }
        else if ("map_bearing".equals(key)) {
        	int bearing = Integer.parseInt(sharedPreferences.getString(key, "0"));
			ApplicationData.getInstance(activity).setMapBearingScale(bearing);
        }
        else if ("mysql_server_url".equals(key)) {
        	String url = sharedPreferences.getString(key, "");
			ApplicationData.getInstance(activity).setMysqlServerUrl(url);
        }
        else if ("methode_calcul_distance".equals(key)) {
    		int methodeCalculDistance = Integer.parseInt(sharedPreferences.getString(key, "1"));
    		ApplicationData.getInstance(activity).setDistanceMethodeCalcul(methodeCalculDistance);
        }

		preference.initializeSummary(key);
	}

}
