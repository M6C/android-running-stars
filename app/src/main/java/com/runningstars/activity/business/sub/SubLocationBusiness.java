package com.runningstars.activity.business.sub;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.runningstars.R;
import com.runningstars.service.system.SystemGpsLocationService;
import com.runningstars.service.system.connection.SystemGpsLocationServiceConnection;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsLocationServiceConnection;
import com.runningstars.service.system.receiver.SystemGpsLocationReceiver;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsLocation;

public class SubLocationBusiness implements ISubBusiness {
	private static final String TAG = SubLocationBusiness.class.getCanonicalName();

	private Context context;
	private INotifierSystemGpsLocationServiceConnection notifierServiceConnection;
	private INotifierSystemGpsLocation notifierService;

	private SystemGpsLocationServiceConnection serviceConnection;
	private SystemGpsLocationReceiver serviseReceiver;

	public SubLocationBusiness(Context context, INotifierSystemGpsLocationServiceConnection notifierServiceConnection, INotifierSystemGpsLocation notifierService) {
		super();
		this.context = context;
		this.notifierServiceConnection = notifierServiceConnection;
		this.notifierService = notifierService;
	}

	/* (non-Javadoc)
	 * @see com.runningstars.activity.business.gpslocation.IGpsBusiness#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		logMe("onCreate");
    	doBindService();
		registerReceiver();
	}

	/* (non-Javadoc)
	 * @see com.runningstars.activity.business.gpslocation.IGpsBusiness#onDestroy()
	 */
	public void onDestroy() {
		logMe("onDestroy");
		unregisterReceiver();
		doUnbindService();
	}

	private void doBindService() {
    	logMe("doBindService");
		if (serviceConnection!=null) {
			logEr("SystemGpsLocationServiceConnection already binded");
			return;
		}

		Intent intent = new Intent(getContext(), SystemGpsLocationService.class);
    	serviceConnection = new SystemGpsLocationServiceConnection(notifierServiceConnection);
    	if (!getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
        	logEr(getContext().getString(R.string.toast_error_service_initialisation_system_gps_location_service));
    		Toast.makeText(getContext(), R.string.toast_error_service_initialisation_system_gps_location_service, Toast.LENGTH_LONG).show();
    	}
    	else {
        	logMe("Location Service binded");
    	}
    }

	private void doUnbindService() {
    	logMe("doUnbindService");
		if (serviceConnection==null) {
			logEr("SystemGpsLocationServiceConnection already unbinded");
			return;
		}

		getContext().unbindService(serviceConnection);
		serviceConnection = null;
    }

	private void registerReceiver() {
		logMe("registerReceiver");
		if (serviseReceiver!=null) {
			logEr("sendSessionProgressReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SystemGpsLocationReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		serviseReceiver = new SystemGpsLocationReceiver(notifierService);
		getContext().registerReceiver(serviseReceiver, filter);
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		if (serviseReceiver==null) {
			logEr("sendSessionProgressReceiver already unregistred");
			return;
		}

		getContext().unregisterReceiver(serviseReceiver);
		serviseReceiver = null;
	}

	private Context getContext() {
		return context;
	}

	/**
     * Log Error methodes
     * @param msg
     */
    private void logEr(String msg) {
		Logger.logEr(TAG, msg);
    }

	/**
     * Log Error methodes
     * @param ex
     */
    @SuppressWarnings("unused")
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
