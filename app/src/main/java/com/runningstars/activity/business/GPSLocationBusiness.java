package com.runningstars.activity.business;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

import com.runningstars.ApplicationData;
import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.activity.GPSMapActivity;
import com.runningstars.activity.business.sub.ISubBusiness;
import com.runningstars.activity.business.sub.SubLocationBusiness;
import com.runningstars.activity.business.sub.SubSessionBusiness;
import com.runningstars.service.system.ISystemGpsLocationService;
import com.runningstars.service.system.ISystemGpsSessionService;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsLocationServiceConnection;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsSessionServiceConnection;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsLocation;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsSession;

public class GPSLocationBusiness implements INotifierSystemGpsSessionServiceConnection, INotifierSystemGpsLocationServiceConnection, INotifierSystemGpsSession, INotifierSystemGpsLocation {
	private static final String TAG = GPSLocationBusiness.class.getCanonicalName();

	private GPSLocationActivity activity;

	private ISubBusiness sessionBusiness;
	private ISubBusiness locationBusiness;

	private ISystemGpsSessionService sessionService;
	private ISystemGpsLocationService locationService;

	private Session session;

	public GPSLocationBusiness(GPSLocationActivity activity) {
		super();
		this.activity = activity;
		this.sessionBusiness = new SubSessionBusiness(activity, this, this);
		this.locationBusiness = new SubLocationBusiness(activity, this, this);
	}

	public void onCreate(Bundle savedInstanceState) {
		logMe("onCreate");

		ApplicationData.getInstance(activity).initializePreference();

    	sessionBusiness.onCreate(savedInstanceState);
    	locationBusiness.onCreate(savedInstanceState);
	}

	public void onDestroy() {
		logMe("onDestroy");
    	sessionBusiness.onDestroy();
    	locationBusiness.onDestroy();
	}

	/**
	 * Exported IPC Methodes
	 */

	public void startListening() {
    	logMe("startListening");

    	doStartService();
    }

    public void stopListening() {
    	logMe("stopListening");

		doStopService();

    	if (getSession()==null) {
    		logEr("session is null");
    		return;
    	}

    	Intent intent = new Intent(activity, GPSMapActivity.class);
		intent.putExtra("ID_SESSION", getSession().getId());
		intent.putExtra("SCALE", 100);
		intent.putExtra("BEARING", 0);
		intent.putExtra("SAVE_SCREENSHOOT", true);
		activity.startActivity(intent);
    }

    public boolean isSystemGpsServiceRunning() {
    	try {
			return locationService!=null && locationService.isListenerRegistred();
		} catch (RemoteException e) {
			logEr(e);
			return false;
		}
    }

    /**
     * Notification Methodes
     */
    
	public void notifySession(Session session) {
    	logMe("notifySession");
		this.session = session;

    	logMe("notifyBroadcast session:"+(session==null ? "null" : session.toString()));

    	activity.notifySession(getSession());
	}

    public void notifyLocation(Localisation localisation) {
    	logMe("notifyLocation");

    	if (localisation==null) {
    		logEr("location is null");
    		return;
    	}

//    	session = localisation.getSession();

    	if (getSession()==null) {
    		logEr("session is null");
    		return;
    	}
    	logMe("notifyLocation localisation:"+localisation.toString());

    	updateSession(localisation);

    	activity.notifyLocation(localisation, getSession());
    }

    /**
     * Setting Service Methodes
     */
    
	public void setSessionService(ISystemGpsSessionService service) {
		logMe("setSessionService");

		this.sessionService = service;

		if (this.sessionService==null) {
			logEr("sessionService is set to null");
			return;
		}

		try {
			logMe("Getting session");
			session = service.getSession();
			activity.notifySession(session);
		} catch (RemoteException e) {
			logEr(e);
		}
	}

	public void setLocationService(ISystemGpsLocationService service) {
		logMe("setLocationService");

		this.locationService = service;

		if (this.locationService==null) {
			logWa("locationService is set to null");
		}

		activity.afterServiceSetted();
	}

	/**
	 * Context Methodes
	 */
	
	public Activity getContext() {
		return activity;
	}

	/**
	 * Private Methodes
	 */

	private void doStartService() {
    	logMe("doStartService");
		if (sessionService==null) {
			logEr("Session Service is null");
			return;
		}

		try {
			sessionService.startService();
		} catch (RemoteException e) {
			logEr(e);
		}
    }

    private void doStopService() {
    	logMe("doStopService");
		if (sessionService==null) {
			logEr("Session Service is null");
			return;
		}

		try {
			sessionService.stopService();
		} catch (RemoteException e) {
			logEr(e);
		}
    }
    
    private void updateSession(Localisation localisation) {
    	logMe("updateSession");
    	
    	session.setCalculateDistance(localisation.getCalculateDistanceCumul());
    	session.setCalculateElapsedTime(localisation.getCalculateElapsedTimeCumul());
    }

    private Session getSession() {
    	return session;
    }

    /**
     * Log Error methodes
     * @param msg
     */
    private void logEr(String msg) {
		Logger.logEr(TAG, msg);
    }

    /**
     * Log Warning methodes
     * @param msg
     */
    private void logWa(String msg) {
		Logger.logWa(TAG, msg);
    }

	/**
     * Log Error methodes
     * @param ex
     */
    private void logEr(Exception ex) {
		Logger.logMe(TAG, ex);
    }

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
