package com.runningstars.service.system;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.R;
import com.runningstars.activity.GPSLocationActivity;
import com.runningstars.business.SessionBusiness;
import com.runningstars.service.system.binder.SystemGpsSessionServiceBinder;
import com.runningstars.service.system.connection.SystemGpsLocationServiceConnection;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsLocationServiceConnection;
import com.runningstars.service.system.receiver.SystemGpsLocationReceiver;
import com.runningstars.service.system.receiver.SystemGpsSessionReceiver;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsLocation;


public class SystemGpsSessionService extends Service implements INotifierMessage, INotifierSystemGpsLocationServiceConnection, INotifierSystemGpsLocation {
	private static final String TAG = SystemGpsSessionService.class.getCanonicalName();

	private SessionBusiness sessionBusiness;
	private Session session;

	private NotificationManager mNM;
    private int NOTIFICATION = R.string.gps_session_service_started;

	private SystemGpsSessionServiceBinder binder;

	private SystemGpsLocationServiceConnection locationServiceConnection;

	private ISystemGpsLocationService locationService;

	private SystemGpsLocationReceiver locationServiseReceiver;

	@Override
	public void onCreate() {
    	logMe("onCreate");
    	sessionBusiness = SessionBusiness.getInstance(getContext(), getNotifier());
		binder = new SystemGpsSessionServiceBinder(this);

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        doBindLocationService();

        super.onCreate();
	}

	@Override
	public void onDestroy() {
	   	logMe("onDestroy");
		doStop();
		doUnbindLocationService();
		binder = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
	   	logMe("onBind");
		return binder;
	}

	@Override
	public ComponentName startService(Intent service) {
    	logMe("startService");
    	if (session==null) {
			doStart();
		}
    	else {
    		logMe("Session is not null");
    	}
		return super.startService(service);
	}

	@Override
	public boolean stopService(Intent name) {
    	logMe("stopService");
		doStop();

		return super.stopService(name);
	}

	public void notifyError(Exception ex) {
	}

	public void notifyMessage(String msg) {
	}

	public void notifyLocation(Localisation localisation) {
    	logMe("notifyLocation");
    	logMe("notifyLocation localisation:"+localisation.toString());
    	
    	session.setCalculateDistance(localisation.getCalculateDistanceCumul());
    	session.setCalculateElapsedTime(localisation.getCalculateElapsedTimeCumul());

    	/**
		 * Update Session Location data
		 */
		if (session.getStart() == null) {
			session.setStart(localisation);

			// Update Session in DB
			sessionBusiness.update(session);
		}

		session.setEnd(localisation);
		
        /**
         * Send Intent to Receiver
         */
		notifyBroadcast(session);
	}

	public Session getSession() {
		return session;
	}

	protected Context getContext() {
		return getApplicationContext();
	}

	/**
	 * Service Private methods
	 */

	private void doStart() {

        /**
         * Create Session
         */
        session = new Session();

        /**
		 * Save Session in DB
		 */
        dbSessionCreate(session);

        /**
         * Register Receiver
         */
        registerReceiver();

        /**
         * Start Location Service
         */
        startLocationService();

        /**
         * Send Intent to Receiver
         */
        notifyBroadcast(session);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
	}

	private void doStop() {

        /**
         * Stop Location Service
         */
		stopLocationService();

		/**
         * Unregister Receiver
         */
        unregisterReceiver();

    	/**
         * Update Session in DB
         */
		if (session!=null && session.getTimeStop()==null) {
			session.setTimeStop(new Date());

			// Update Session in DB
			sessionBusiness.updateAndCalculate(session);
		}

        /**
         * Send Intent to Receiver
         */
        notifyBroadcast(null);

        // Cancel notification.
		hideNotification();
	}

    private void doBindLocationService() {
    	logMe("doBindLocationService");

		if (locationServiceConnection!=null) {
			logEr("SystemGpsLocationServiceConnection already binded");
			return;
		}

		Intent intent = new Intent(getContext(), SystemGpsLocationService.class);
    	locationServiceConnection = new SystemGpsLocationServiceConnection(this);
    	if (!getContext().bindService(intent, locationServiceConnection, Context.BIND_AUTO_CREATE)) {
        	logEr(getContext().getString(R.string.toast_error_service_initialisation_system_gps_location_service));
    		Toast.makeText(getContext(), R.string.toast_error_service_initialisation_system_gps_location_service, Toast.LENGTH_LONG).show();
    	}
    	else {
        	logMe("Location Service binded");
    	}
    }

    private void doUnbindLocationService() {
    	logMe("doUnbindLocationService");
		if (locationServiceConnection==null) {
			logEr("SystemGpsLocationServiceConnection already unbinded");
			return;
		}

		getContext().unbindService(locationServiceConnection);
		locationServiceConnection = null;
    }

	public void setLocationService(ISystemGpsLocationService service) {
		logMe("setLocationService");

		this.locationService = service;

		if (this.locationService==null) {
			logWa("locationService is set to null");
		}
	}

	private void registerReceiver() {
		logMe("registerReceiver");
		if (locationServiseReceiver!=null) {
			logEr("sendSessionProgressReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SystemGpsLocationReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		locationServiseReceiver = new SystemGpsLocationReceiver(this);
		getContext().registerReceiver(locationServiseReceiver, filter);
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		if (locationServiseReceiver==null) {
			logEr("locationServiseReceiver already unregistred");
			return;
		}

		getContext().unregisterReceiver(locationServiseReceiver);
		locationServiseReceiver = null;
	}

	private void notifyBroadcast(Session session) {
		logMe("notifyBroadCast");
    	logMe("notifyBroadcast session:"+(session==null ? "null" : session.toString()));
		Intent intent = new Intent(SystemGpsSessionReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(SystemGpsSessionReceiver.INTENT_KEY_SESSION, (Parcelable)session);
		sendBroadcast(intent);
	}

	private void startLocationService() {
		logMe("startLocationService");
		try {
			locationService.startService(session);
		} catch (RemoteException e) {
			logEr(e);
		}
	}

	private void stopLocationService() {
		logMe("stopLocationService");
		try {
			locationService.stopService();
		} catch (RemoteException e) {
			logEr(e);
		}
	}

	private void dbSessionCreate(Session session) {
		logMe("dbSessionCreate");
		// Update Session in DB
		sessionBusiness.create(session);
    }

	private INotifierMessage getNotifier() {
		return this;
	}

	/**
     * Show a notification while this service is running.
     */
    @SuppressWarnings("deprecation")
	private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(NOTIFICATION);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.session_to_send_01, text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, GPSLocationActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        setLatestEventInfo(getContext(), notification, getText(R.string.gps_session_service_label), text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    private void setLatestEventInfo(Context context, Notification notification, CharSequence contentTitle, CharSequence text, PendingIntent pendingIntent) {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			try {
				Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				deprecatedMethod.invoke(notification, context, contentTitle, null, pendingIntent);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Log.w(TAG, "Method not found", e);
			}
		} else {
			// Use new API
			Notification.Builder builder = new Notification.Builder(context)
					.setContentIntent(pendingIntent)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(contentTitle);
			notification = builder.build();
		}	}
	/**
     * Show a notification while this service is running.
     */
    private void hideNotification() {
        // Cancel the notification.
        mNM.cancel(NOTIFICATION);
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