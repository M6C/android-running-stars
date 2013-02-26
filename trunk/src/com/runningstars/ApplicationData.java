package com.runningstars;

import org.gdocument.gtracergps.GpsConstant;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.runningstars.tool.ToolLocationManager;
import com.runningstars.tool.ToolSharedPreferences;

public class ApplicationData {
	
	private static ApplicationData instance;
	private static Context context;
	private static int gpsLocationMapZoom = 20;
	private static int mapScreenShootQuality = 50; //0-100 : 0 = low quality for small image, 100 = high quality
	private static int mapScaleInMeter = 50; //0-100 : 0 = low quality for small image, 100 = high quality
	private static int mapBearingScale = 50; //0-100 : 0 = low quality for small image, 100 = high quality
	private static METHODE_CALCUL_DISTANCE distanceMethodeCalcul = METHODE_CALCUL_DISTANCE.METHODE_1; //0-100 : 0 = low quality for small image, 100 = high quality

	private String mysql_server_url = "";
	private String mysql_server_internet_url = "";
	private String mysql_server_intranet_url = "";

	public enum METHODE_CALCUL_DISTANCE {
		METHODE_1, METHODE_2, METHODE_3
	}
	
	private ApplicationData() {
		if (context!=null) {
			mysql_server_internet_url = context.getResources().getString(R.string.default_mysql_server_internet_url);
			mysql_server_intranet_url = context.getResources().getString(R.string.default_mysql_server_intranet_url);
			mysql_server_url = (ToolLocationManager.getInstance(context).isNetworkEnabled() ? mysql_server_intranet_url : mysql_server_internet_url);
		}
	}

	public static ApplicationData getInstance(Context context) {
		if (context!=null)
			ApplicationData.context = context;

		if (instance == null)
			instance = new ApplicationData();

		return instance;
	}

    /**
     * Reading Application Data from shared preferences
     */
	public void initializePreference() {
		if (context!=null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	
			GpsConstant.LOG_WRITE = ToolSharedPreferences.getBoolean(sharedPreferences, "perform_log", false);
			GpsConstant.LOG_WRITE_SYSOUT = ToolSharedPreferences.getBoolean(sharedPreferences, "perform_log_sysout", false);
			GpsConstant.LOG_WRITE_SD = ToolSharedPreferences.getBoolean(sharedPreferences, "perform_log_sd", false);
	
			if (GpsConstant.LOG_WRITE && GpsConstant.LOG_WRITE_SD) {
				Logger.initLogSD();
			}
	
			int screebshootQuality = Integer.parseInt(ToolSharedPreferences.getString(sharedPreferences, "screenshoot_quality", "25"));
			ApplicationData.getInstance(context).setMapScreenShootQuality(screebshootQuality);
	
			int progress = Integer.parseInt(ToolSharedPreferences.getString(sharedPreferences, "map_zoom", "20"));
			ApplicationData.getInstance(context).setGpsLocationMapZoom(progress);
	
			int mapScale = Integer.parseInt(ToolSharedPreferences.getString(sharedPreferences, "map_scale", "50"));
			ApplicationData.getInstance(context).setMapScaleInMeter(mapScale);
	
			int mapBearing = Integer.parseInt(ToolSharedPreferences.getString(sharedPreferences, "map_bearing", "50"));
			ApplicationData.getInstance(context).setMapBearingScale(mapBearing);
	
			String url = ToolSharedPreferences.getString(sharedPreferences, "mysql_server_url", context.getResources().getStringArray(R.array.mysqlServerUrlValues)[0]);
			ApplicationData.getInstance(context).setMysqlServerUrl(url);
	
			int methodeCalculDistance = Integer.parseInt(ToolSharedPreferences.getString(sharedPreferences, "methode_calcul_distance", "1"));
			ApplicationData.getInstance(context).setDistanceMethodeCalcul(methodeCalculDistance);
		}
	}

	/**
	 * Obtaine Mysql server Url
	 * @return
	 */
	public String getMysqlServerUrl() {
		return mysql_server_url;
	}

	/**
	 * Update Mysql server Url
	 */
	public void setMysqlServerUrl(String url) {
		mysql_server_url = url;
	}

	/**
	 * @return the gpsLocationMapZoom
	 */
	public int getGpsLocationMapZoom() {
		return gpsLocationMapZoom;
	}

	/**
	 * @param gpsLocationMapZoom the gpsLocationMapZoom to set
	 */
	public void setGpsLocationMapZoom(int gpsLocationMapZoom) {
		ApplicationData.gpsLocationMapZoom = gpsLocationMapZoom;
	}

	/**
	 * @return the mapScreenShootQuality
	 */
	public int getMapScreenShootQuality() {
		return mapScreenShootQuality;
	}

	/**
	 * @param mapScreenShootQuality the mapScreenShootQuality to set
	 */
	public void setMapScreenShootQuality(int mapScreenShootQuality) {
		ApplicationData.mapScreenShootQuality = mapScreenShootQuality;
	}

	/**
	 * @return the mapScaleInMeter
	 */
	public int getMapScaleInMeter() {
		return mapScaleInMeter;
	}

	/**
	 * @param mapScaleInMeter the mapScaleInMeter to set
	 */
	public void setMapScaleInMeter(int mapScaleInMeter) {
		ApplicationData.mapScaleInMeter = mapScaleInMeter;
	}

	/**
	 * @return the mapBearingScale
	 */
	public int getMapBearingScale() {
		return mapBearingScale;
	}

	/**
	 * @param mapBearingScale the mapBearingScale to set
	 */
	public void setMapBearingScale(int mapBearingScale) {
		ApplicationData.mapBearingScale = mapBearingScale;
	}

	/**
	 * @return the distanceMethodeCalcul
	 */
	public METHODE_CALCUL_DISTANCE getDistanceMethodeCalcul() {
		return distanceMethodeCalcul;
	}

	/**
	 * @param distanceMethodeCalcul the distanceMethodeCalcul to set
	 */
	public void setDistanceMethodeCalcul(METHODE_CALCUL_DISTANCE distanceMethodeCalcul) {
		ApplicationData.distanceMethodeCalcul = distanceMethodeCalcul;
	}

	/**
	 * @param methode the distanceMethodeCalcul to set
	 */
	public void setDistanceMethodeCalcul(int methode) {
		switch(methode) {
			case 3:
				setDistanceMethodeCalcul(METHODE_CALCUL_DISTANCE.METHODE_3);
				break;
			case 2:
				setDistanceMethodeCalcul(METHODE_CALCUL_DISTANCE.METHODE_2);
				break;
			default:
				setDistanceMethodeCalcul(METHODE_CALCUL_DISTANCE.METHODE_1);
		}
	}
}
