package com.runningstars.activity.business;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Intent;
import android.content.IntentFilter;

import com.cameleon.common.factory.FactoryNotifier;
import com.runningstars.activity.DbToolActivity;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;
import com.runningstars.service.db.DbBackupService;
import com.runningstars.service.db.DbRecreateService;
import com.runningstars.service.db.DbRestoreService;
import com.runningstars.service.session.SessionSendService;
import com.runningstars.service.session.receiver.SessionProgressReceiver;
import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;

public class DbToolBusiness {
	private static final String TAG = DbToolBusiness.class.getCanonicalName();

	private DbToolActivity activity;
	private DBSessionDataSource dbSessionDatasource;

	private SessionProgressReceiver sendSessionProgressReceiver;

	public DbToolBusiness(DbToolActivity activity) {
		super();
		this.activity = activity;

        dbSessionDatasource = new DBSessionDataSource(getContext(), activity);
	}

	public DbToolActivity getContext() {
		return activity;
	}

	public void onResume() {
		logMe("onResume");
		registerReceiverProgress();
	}

	public void onPause() {
		logMe("onPause");
		unregisterReceiverProgress();
	}

	private INotifierSessionProgress getNotifierSendSessionProgress() {
		return FactoryNotifier.getInstance().getNotifierSendSessionProgress(activity);
	}

	private void registerReceiverProgress() {
		logMe("registerReceiverProgress");
		if (sendSessionProgressReceiver!=null) {
			logEr("sendSessionProgressReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SessionProgressReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		sendSessionProgressReceiver = new SessionProgressReceiver(getNotifierSendSessionProgress());
		activity.registerReceiver(sendSessionProgressReceiver, filter);
	}

	private void unregisterReceiverProgress() {
		logMe("unregisterReceiverProgress");
		if (sendSessionProgressReceiver==null) {
			logEr("sendSessionProgressReceiver already unregistred");
			return;
		}

		activity.unregisterReceiver(sendSessionProgressReceiver);
		sendSessionProgressReceiver = null;
	}

    public void sendSessionNotSend() {
//    	SessionBusiness sessionBusiness = SessionBusiness.getInstance(getContext(), activity, activity.getHandler());
//    	LocationBusiness locationBusiness = LocationBusiness.getInstance(getContext(), activity, activity.getHandler());
//
//    	List<Session> listSession = dbSessionNotSend();
//    	for(final Session session : listSession) {
//    		sessionBusiness.sendSession(session);
//    		locationBusiness.sendLocation(session);
//    	}

    	List<Session> listSession = dbSessionNotSend();
    	for(final Session session : listSession) {
        	Intent intent = new Intent(getContext(), SessionSendService.class);
        	intent.putExtra(SessionSendService.INTENT_KEY_SESSION, session.getId());
        	getContext().startService(intent);
    	}
    }

    /**
     * Database methode
     */
    public void dbBackup() {
    	Intent intent = new Intent(getContext(), DbBackupService.class);
    	getContext().startService(intent);
    }

    /**
     * Database methode
     */
    public void dbRestore() {
    	Intent intent = new Intent(getContext(), DbRestoreService.class);
    	getContext().startService(intent);
    }

    /**
     * Drop Table methode
     */
    public void recreateTable() {
    	Intent intent = new Intent(getContext(), DbRecreateService.class);
    	getContext().startService(intent);
//    	/**
//    	 * Recreate Location DB
//    	 */
//    	try {
//            dbLocationDatasource.open();
//        	dbLocationDatasource.recreateTable();
//
//        	/**
//        	 * Recreate Session DB
//        	 */
//        	try {
//                dbSessionDatasource.open();
//            	dbSessionDatasource.recreateTable();
//        	}
//        	catch(Exception ex) {
//        		activity.notifyError(ex);
//        	}
//        	finally {
//                dbSessionDatasource.close();
//        	}
//    	}
//    	catch(Exception ex) {
//    		activity.notifyError(ex);
//    	}
//    	finally {
//            dbLocationDatasource.close();
//    	}
    }

    private List<Session> dbSessionNotSend() {
    	try {
	        dbSessionDatasource.open();
			// Get in database
			return dbSessionDatasource.getNotSend();
    	}
    	finally {
            dbSessionDatasource.close();
    	}
    }

    public void dbSessionUpdateSend(Session session) {
    	try {
	        dbSessionDatasource.open();
			// Update in database
			dbSessionDatasource.updateSend(session);
    	}
    	finally {
            dbSessionDatasource.close();
    	}
    }

	/**
     * Log Error methodes
     * @param msg
     */
    private void logEr(String msg) {
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
