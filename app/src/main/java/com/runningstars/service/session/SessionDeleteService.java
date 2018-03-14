package com.runningstars.service.session;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cameleon.common.factory.FactoryNotifier;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;

public class SessionDeleteService extends IntentService {
	private static final String TAG = SessionDeleteService.class.getCanonicalName();

	public static final String SERVICE_NAME = "SESSION_DELETE_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	private DBSessionDataSource dbSessionDatasource;
	private DBLocationDataSource dbLocationDatasource;

	public SessionDeleteService() {
		super(SERVICE_NAME);

        dbSessionDatasource = new DBSessionDataSource(getContext(), getNotifier());
        dbLocationDatasource = new DBLocationDataSource(getContext(), getNotifier());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");

		long idSession = intent.getLongExtra(INTENT_KEY_SESSION, -1);
		if (idSession>-1) {
			delete(idSession);
		}
	}

	private void delete(long idSession) {
		try {
	        dbSessionDatasource.open();

	        Session session = dbSessionDatasource.getById(idSession);
	        dbSessionDatasource.delete(session);

	        dbLocationDatasource.open();
	        dbLocationDatasource.delete(session);
		}
		finally {
	        dbSessionDatasource.close();
	        dbLocationDatasource.close();
		}
	}

	private Context getContext() {
		return this;
	}

	private INotifierMessage getNotifier() {
		return FactoryNotifier.getInstance().getNotifierToast(getContext());
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
