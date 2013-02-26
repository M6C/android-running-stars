package com.runningstars.service.system;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;

import com.runningstars.ApplicationData;
import com.runningstars.ApplicationData.METHODE_CALCUL_DISTANCE;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.service.system.binder.SystemGpsLocationServiceBinder;
import com.runningstars.service.system.listener.SystemGpsLocationListener;
import com.runningstars.service.system.receiver.SystemGpsLocationReceiver;
import com.runningstars.service.system.receiver.SystemGpsSessionReceiver;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsSession;
import com.runningstars.tool.ToolCalculate;
import com.runningstars.tool.ToolLocationManager;

public class SystemGpsLocationService extends Service implements INotifierMessage, INotifierSystemGpsSession {

	private static final String TAG = SystemGpsLocationService.class.getCanonicalName();
	public static final String INTENT_KEY_LOCALISATION = "INTENT_LOCATION"; 

	private DBLocationDataSource dbLocationDatasource;

	private Localisation previousLocalisation;
	private SystemGpsLocationServiceBinder binder;
	private SystemGpsLocationListener listener;

	private SystemGpsSessionReceiver sessionServiseReceiver;
	private Session session;

	@Override
	public void onCreate() {
    	logMe("onCreate");
		dbLocationDatasource = new DBLocationDataSource(getContext(), getNotifier());
		binder = new SystemGpsLocationServiceBinder(this);

        super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
    	logMe("onDestroy");
		
		super.onDestroy();
	}

	@Override
	public boolean stopService(Intent name) {
    	logMe("stopService");

		if (session==null) {
			logWa("Session is already null");
		}
    	session = null;

    	unregisterReceiver();
		unregisterListener();

    	return super.stopService(name);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
    	logMe("onStart");

    	if (intent==null) {
	    	logEr("Intent is null");
	    	return;
    	}

    	if (intent.hasExtra(SystemGpsSessionReceiver.INTENT_KEY_SESSION)) {
			logMe("Session Intent");
    		session = intent.getParcelableExtra(SystemGpsSessionReceiver.INTENT_KEY_SESSION);
    		if (session==null) {
    			logWa("Session is null");
    		}
    		registerReceiver();
			registerListener();
		}
		else if (intent.hasExtra(SystemGpsLocationService.INTENT_KEY_LOCALISATION)) {
			logMe("Location Intent");
	    	Localisation localisation = intent.getParcelableExtra(SystemGpsLocationService.INTENT_KEY_LOCALISATION);
			doUpdate(localisation);
		}
		else {
			logWa("Intent with no extra data");
		}
	}

	public boolean isListenerRegistred() {
		return listener!=null;
	}

	/**
	 * Notification methods
	 */
	public void notifyError(Exception ex) {
	}

	public void notifyMessage(String msg) {
	}

	public void notifySession(Session session) {
		logMe("notifySession");
		if (session==null) {
			logWa("Session is null");
		}
		this.session = session;
	}

    /**
     * Register Gps Listener
     */

	private void registerListener() {
		logMe("registerListener");
		if (isListenerRegistred()) {
			logEr("Listener is already registred");
			return;
		}

//		Looper.prepare();
//		ThreadGroup group = Thread.currentThread().getThreadGroup();
//		new Thread(group, new Runnable() {
		new Thread(new Runnable() {
			public void run() {
				listener = new SystemGpsLocationListener(getContext());
				ToolLocationManager.getInstance(getContext()).requestGpsUpdates(listener);
			}
		}).run();
	}

	private void unregisterListener() {
		logMe("unregisterListener");
		if (!isListenerRegistred()) {
			logEr("Listener is already unregistred");
			return;
		}

//		Looper.prepare();
//		ThreadGroup group = Thread.currentThread().getThreadGroup();
//		new Thread(group, new Runnable() {
		new Thread(new Runnable() {
			public void run() {
				ToolLocationManager.getInstance(getContext()).removeUpdates(listener);
				listener = null;
			}
		}).run();
	}

	/**
	 * Service Private methods
	 */
	private void doUpdate(Localisation localisation) {
		logMe("doUpdate");

    	if (localisation==null) {
			logEr("Location is null");
			return;
    	}

    	logMe("doUpdate localisation:"+localisation.toString());

		if (session==null) {
			logEr("Session is null");
			return;
		}

    	if (previousLocalisation==null) {
			logWa("previousLocation is null");
    	}

    	if (previousLocalisation!=null) {
    		double distance = 0.0d;
//    		if (previousLocalisation.getLatitude() != localisation.getLatitude() || 
//    			previousLocalisation.getLongitude() != localisation.getLongitude() ||
//				previousLocalisation.getAltitude() != localisation.getAltitude())
//    		{

	    		distance = ToolCalculate.getDistance(
	    			ApplicationData.getInstance(this).getDistanceMethodeCalcul(),
					previousLocalisation.getLatitude(), previousLocalisation.getLongitude(), 
					localisation.getLatitude(), localisation.getLongitude(),
					previousLocalisation.getAltitude(), localisation.getAltitude()
				);
//    		}

    		long elapsedTime = (localisation.getTime()>0 && previousLocalisation.getTime()>0) ? localisation.getTime() - previousLocalisation.getTime() : 0;
    		long elapsedTimeTotal = previousLocalisation.getCalculateElapsedTimeCumul() + elapsedTime;//new Date().getTime() - session.getTimeStart().getTime();

    		double distanceTotal = previousLocalisation.getCalculateDistanceCumul() + distance;//session.getCalculateDistance() + distance;
    		float speed = (distance<=0.0d || elapsedTime<=0) ? 0.0f : (float) ((distance / elapsedTime) * 100000.0f); 
    		float speedAverage = (distanceTotal<=0.0d || elapsedTimeTotal<=0) ? 0.0f : (float) ((distanceTotal / elapsedTimeTotal) * 100000.0f); 
    		
    		localisation.setCalculateElapsedTime(elapsedTime);
    		localisation.setCalculateElapsedTimeCumul(elapsedTimeTotal);

    		localisation.setCalculateDistance(distance);
    		localisation.setCalculateDistanceCumul(distanceTotal);
    		localisation.setCalculateSpeed(speed);
    		localisation.setCalculateSpeedAverage(speedAverage);

    		logMe("doUpdate after calculate localisation:"+localisation.toString());
    	}
    	previousLocalisation = localisation;


		/**
		 * Init Location Session data
		 */
		localisation.setSession(session);

		/**
		 * Save Location in DB
		 */
		dbLocationCreate(localisation);

		//Send Intent to Receiver
    	notifyBroadcast(localisation);
	}

    private void dbLocationCreate(Localisation localisation) {
		logMe("dbLocationCreate");
    	try {
    		dbLocationDatasource.open();
    		// Save in database
    		dbLocationDatasource.create(localisation);
    	}
    	finally {
    		dbLocationDatasource.close();
    	}
    }

	private void registerReceiver() {
		logMe("registerReceiver");
		if (sessionServiseReceiver!=null) {
			logEr("systemGpsSessionReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SystemGpsSessionReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		sessionServiseReceiver = new SystemGpsSessionReceiver(this);
		getContext().registerReceiver(sessionServiseReceiver, filter);
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		if (sessionServiseReceiver==null) {
			logEr("sessionServiseReceiver already registred");
			return;
		}

		getContext().unregisterReceiver(sessionServiseReceiver);
		sessionServiseReceiver = null;
	}

    private void notifyBroadcast(Localisation localisation) {
		logMe("notifyBroadcast");
    	logMe("notifyBroadcast localisation:"+localisation.toString());
		Intent intent = new Intent(SystemGpsLocationReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(SystemGpsLocationReceiver.INTENT_KEY_LOCALISATION, (Parcelable)localisation);
		sendBroadcast(intent);
	}

	private Context getContext() {
		return this;
	}

    private INotifierMessage getNotifier() {
    	return this;
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
		Logger.logEr(TAG, msg);
    }

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
