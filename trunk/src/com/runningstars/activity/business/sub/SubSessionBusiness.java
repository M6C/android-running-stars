package com.runningstars.activity.business.sub;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.runningstars.R;
import com.runningstars.service.system.SystemGpsSessionService;
import com.runningstars.service.system.connection.SystemGpsSessionServiceConnection;
import com.runningstars.service.system.connection.inotifier.INotifierSystemGpsSessionServiceConnection;
import com.runningstars.service.system.receiver.SystemGpsSessionReceiver;
import com.runningstars.service.system.receiver.inotifier.INotifierSystemGpsSession;

public class SubSessionBusiness implements ISubBusiness {
	private static final String TAG = SubSessionBusiness.class.getCanonicalName();


	private Context context;
	private INotifierSystemGpsSessionServiceConnection notifierServiceConnection;
	private INotifierSystemGpsSession notifierService;

	private SystemGpsSessionServiceConnection serviceConnection;
	private SystemGpsSessionReceiver serviseReceiver;

	public SubSessionBusiness(Context context, INotifierSystemGpsSessionServiceConnection notifierServiceConnection, INotifierSystemGpsSession notifierService) {
		super();
		this.context = context;
		this.notifierServiceConnection = notifierServiceConnection;
		this.notifierService = notifierService;
	}

	public void onCreate(Bundle savedInstanceState) {
		logMe("onCreate");
    	doBindService();
		registerReceiver();
	}

	public void onDestroy() {
		logMe("onDestroy");
		unregisterReceiver();
		doUnbindService();
	}

    private void doBindService() {
    	logMe("doBindService");
		if (serviceConnection!=null) {
			logEr("SystemGpsSessionServiceConnection already binded");
			return;
		}

		Intent intent = new Intent(getContext(), SystemGpsSessionService.class);
    	serviceConnection = new SystemGpsSessionServiceConnection(notifierServiceConnection);
    	if (!getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
        	logEr(getContext().getString(R.string.toast_error_service_initialisation_system_gps_session_service));
    		Toast.makeText(getContext(), R.string.toast_error_service_initialisation_system_gps_session_service, Toast.LENGTH_LONG).show();
    	}
    	else {
        	logMe("Session Service binded");
    	}
    }

    private void doUnbindService() {
    	logMe("doUnbindService");
		if (serviceConnection==null) {
			logEr("SystemGpsSessionServiceConnection already unbinded");
			return;
		}

		getContext().unbindService(serviceConnection);
    	serviceConnection = null;
    }

	private void registerReceiver() {
		logMe("registerReceiver");
		if (serviseReceiver!=null) {
			logEr("systemGpsSessionReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SystemGpsSessionReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		serviseReceiver = new SystemGpsSessionReceiver(notifierService);
		getContext().registerReceiver(serviseReceiver, filter);
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		if (serviseReceiver==null) {
			logEr("systemGpsSessionReceiver already registred");
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
