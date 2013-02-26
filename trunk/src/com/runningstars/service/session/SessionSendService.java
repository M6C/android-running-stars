package com.runningstars.service.session;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.runningstars.R;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.business.LocationBusiness;
import com.runningstars.business.SessionBusiness;
import com.runningstars.factory.FactoryNotifier;
import com.runningstars.service.session.receiver.SessionProgressReceiver;

public class SessionSendService extends IntentService {
	private static final String TAG = SessionSendService.class.getCanonicalName();

	public static final String SERVICE_NAME = "SESSION_SEND_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	private SessionBusiness sessionBusiness;
	private LocationBusiness locationBusiness;

	public SessionSendService() {
		super(SERVICE_NAME);

		locationBusiness = LocationBusiness.getInstance(getContext(), getNotifier());
    	sessionBusiness = SessionBusiness.getInstance(getContext(), getNotifier());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");

		long idSession = intent.getLongExtra(INTENT_KEY_SESSION, -1);
		if (idSession>-1) {
			send(idSession);
		}
	}

	private void send(long idSession) {
		notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_SESSION, R.string.send_session_message_session, -1, -1);

    	try {
    		Session session = sessionBusiness.getById(idSession);
	    	// Clean data send
	    	session.setIdSend(0);
	    	session.setTimeSend(null);
	    	sessionBusiness.updateSend(session);
	    	locationBusiness.updateTimeSend(session, session.getTimeSend());

	    	sessionBusiness.sendSession(session);
			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_LOCATION, R.string.send_session_message_locations, -1, -1);

			/**
			 * Create LOCATION
			 */

			long iStart = locationBusiness.getMinIdBySession(idSession);
			long iEnd = locationBusiness.getMaxIdBySession(idSession);
			int iStep = 25;

			int nb = 0, total = (int)(iEnd-iStart);
			for(long i=iStart ; i<=iEnd ; i+=iStep, nb += iStep) {
				notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_LOCATION, R.string.send_session_message_locations_count/* String.format("Sending locations %1$d/%2$d", nb, total)*/, nb, total);
				locationBusiness.sendLocation(session, i, i+iStep-1);
			}
    	}
    	finally {
			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_END, R.string.send_session_message_finished, -1, -1);
    	}
	}

	private void notifyBroadcastProgress(int progress, int messageId, int nb, int total) {
		Intent intent = new Intent(SessionProgressReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE, progress);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_MESSAGE, messageId);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_COUNT, nb); 
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_TOTAL, total); 
		sendBroadcast(intent);
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
