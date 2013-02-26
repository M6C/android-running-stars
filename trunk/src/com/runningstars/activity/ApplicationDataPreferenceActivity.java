package com.runningstars.activity;

import org.gdocument.gtracergps.GpsConstant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.listener.ontouch.ParameterOnTouchListener;
import com.runningstars.listener.preference.ApplicationDataPreferenceListener;

public class ApplicationDataPreferenceActivity extends PreferenceActivity {

	private ParameterOnTouchListener onTouchListener;
	private SharedPreferences.OnSharedPreferenceChangeListener spChanged;
	private Preference performLog;
	private Preference performLogSysOut;
	private Preference performLogSd;
	private ListPreference screenshootQuality;
	private ListPreference mapZoom;
	private ListPreference mapScale;
	private ListPreference mapBearing;
	private ListPreference mapServerUrl;
	private ListPreference methodeCalculDistance;

	@Override
	public void onCreate(Bundle savedInstanceState) {    
	    super.onCreate(savedInstanceState);

	    addPreferencesFromResource(R.xml.application_data_preference);       

//		FactoryStyle.getInstance().centerTitle(this);

		//http://stackoverflow.com/questions/9505901/android-using-switch-preference-pre-api-level-14
	    performLog = (Preference)getPreferenceScreen().findPreference("perform_log");
	    performLogSysOut = (Preference)getPreferenceScreen().findPreference("perform_log_sysout");
	    performLogSd = (Preference)getPreferenceScreen().findPreference("perform_log_sd");
	    screenshootQuality = (ListPreference)getPreferenceScreen().findPreference("screenshoot_quality");
	    mapZoom = (ListPreference)getPreferenceScreen().findPreference("map_zoom");
	    mapScale = (ListPreference)getPreferenceScreen().findPreference("map_scale");
	    mapBearing = (ListPreference)getPreferenceScreen().findPreference("map_bearing");
	    mapServerUrl = (ListPreference)getPreferenceScreen().findPreference("mysql_server_url");
	    methodeCalculDistance = (ListPreference)getPreferenceScreen().findPreference("methode_calcul_distance");

	    initializeSummary(null); 

		enableOnTouchListenerMain();

	    spChanged = new ApplicationDataPreferenceListener(this);
	}

	/**
     * Setup the initial values
	 */
	public void initializeSummary(String key) {
//		SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
		if (key==null || "perform_log".equals(key))
//			performLog.setSummary(sharedPreferences.getBoolean("perform_log", false) ? "Disabled Log" : "Enabled Log");
			performLog.setSummary(GpsConstant.LOG_WRITE ? "Enabled Log" : "Disabled Log");
		if (key==null || "perform_log_sysout".equals(key))
//			performLogSysOut.setSummary(sharedPreferences.getBoolean("perform_log_sysout", false) ? "Disabled Log SysOut" : "Enabled Log SysOut");
			performLogSysOut.setSummary(GpsConstant.LOG_WRITE_SYSOUT ? "Enabled Log SysOut" : "Disabled Log SysOut");
		if (key==null || "perform_log_sd".equals(key))
//			performLogSd.setSummary(sharedPreferences.getBoolean("perform_log_sd", false) ? "Disabled Log Sd" : "Enabled Log Sd");
			performLogSd.setSummary(GpsConstant.LOG_WRITE_SD ? "Enabled Log Sd" : "Disabled Log Sd");
		if (key==null || "screenshoot_quality".equals(key))
//			screenshootQuality.setSummary("Define quality of screenshoot map " + sharedPreferences.getString("screenshoot_quality", "")); 
			screenshootQuality.setSummary("Define quality of screenshoot map : " + ApplicationData.getInstance(this).getMapScreenShootQuality()); 
		if (key==null || "map_zoom".equals(key))
//			mapZoom.setSummary("Define the map zoom " + sharedPreferences.getString("map_zoom", "")); 
			mapZoom.setSummary("Define the map zoom : " + ApplicationData.getInstance(this).getGpsLocationMapZoom()); 
		if (key==null || "map_scale".equals(key))
//			mapScale.setSummary("Define the map scale " + sharedPreferences.getString("map_scale", "")); 
			mapScale.setSummary("Define the map scale : " + ApplicationData.getInstance(this).getMapScaleInMeter()); 
		if (key==null || "map_bearing".equals(key))
//			mapBearing.setSummary("Define the map bearing " + sharedPreferences.getString("map_bearing", "")); 
			mapBearing.setSummary("Define the map bearing : " + ApplicationData.getInstance(this).getMapBearingScale()); 
		if (key==null || "mysql_server_url".equals(key))
//			mapServerUrl.setSummary("Define MySql Server Url " + sharedPreferences.getString("mysql_server_url", ""));
			mapServerUrl.setSummary("Define MySql Server Url : " + ApplicationData.getInstance(this).getMysqlServerUrl());
		if (key==null || "methode_calcul_distance".equals(key))
//			mapServerUrl.setSummary("Define MySql Server Url " + sharedPreferences.getString("mysql_server_url", ""));
			methodeCalculDistance.setSummary("Define distance methode calculate : " + ApplicationData.getInstance(this).getDistanceMethodeCalcul());
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(spChanged);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(spChanged);
	}

	private void enableOnTouchListenerMain() {

		if (onTouchListener == null)
			onTouchListener = new ParameterOnTouchListener(this);

		// Set the touch listener for the main view to be our custom gesture
		// listener
		this.getListView().setOnTouchListener(onTouchListener);
	}
}
