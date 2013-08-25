package com.runningstars.service.db;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;
import com.runningstars.factory.FactoryNotifier;

public class DbRestoreService extends IntentService {
	private static final String TAG = DbRestoreService.class.getCanonicalName();

	public static final String SERVICE_NAME = "DB_RESTORE_SERVICE"; 

	private DBLocationDataSource dbLocationDatasource;
	private DBSessionDataSource dbSessionDatasource;

	public DbRestoreService() {
		super(SERVICE_NAME);

        dbLocationDatasource = new DBLocationDataSource(getContext(), getNotifier());
        dbSessionDatasource = new DBSessionDataSource(getContext(), getNotifier());
	}

	private Context getContext() {
		return this;
	}

	private INotifierMessage getNotifier() {
		return FactoryNotifier.getInstance().getNotifierToast(getContext());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");
		dbRestore();
	}

    /**
     * Database methode
     */
    private void dbRestore() {
    	/**
    	 * Backup Location DB
    	 */
    	try {
            dbLocationDatasource.open();
    		dbLocationDatasource.restoreDbFromSdcard();
    	}
    	catch(Exception ex) {
    		getNotifier().notifyError(ex);
    	}
    	finally {
            dbLocationDatasource.close();
    	}

    	/**
    	 * Backup Session DB
    	 */
    	try {
            dbSessionDatasource.open();
        	dbSessionDatasource.restoreDbFromSdcard();
    	}
    	catch(Exception ex) {
    		getNotifier().notifyError(ex);
    	}
    	finally {
            dbSessionDatasource.close();
    	}
    }

    /**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
