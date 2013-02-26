package com.runningstars.application;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Application;
import android.content.Context;

//http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
public class GPSApplication extends Application {
	private static final String TAG = GPSApplication.class.getCanonicalName();

    private static Context context;

    public void onCreate(){
		logMe("onCreate");
        GPSApplication.context = getApplicationContext();
        super.onCreate();
    }

    public static Context getAppContext() {
        return GPSApplication.context;
    }

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
