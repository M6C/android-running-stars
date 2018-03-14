package com.runningstars.tool;

import android.content.SharedPreferences;

public class ToolSharedPreferences {

	/**
	 * 
	 * @param sharedPreferences
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(SharedPreferences sharedPreferences, String key, String defaultValue) {
		String ret = defaultValue;
		if (sharedPreferences.contains(key)) {
			ret = sharedPreferences.getString(key, defaultValue);
		}
		else {
			sharedPreferences.edit().putString(key, defaultValue);
		}
		return ret;
	}

	/**
	 * 
	 * @param sharedPreferences
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(SharedPreferences sharedPreferences, String key, boolean defaultValue) {
		boolean ret = defaultValue;
		if (sharedPreferences.contains(key)) {
			ret = sharedPreferences.getBoolean(key, defaultValue);
		}
		else {
			sharedPreferences.edit().putBoolean(key, defaultValue);
		}
		return ret;
	}
}
