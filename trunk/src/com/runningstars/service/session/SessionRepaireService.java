package com.runningstars.service.session;

import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.business.LocationBusiness;
import com.runningstars.business.SessionBusiness;
import com.runningstars.factory.FactoryNotifier;
import com.runningstars.service.session.receiver.SessionProgressReceiver;
import com.runningstars.tool.ToolCalculate;

public class SessionRepaireService extends IntentService {
	private static final String TAG = SessionRepaireService.class.getCanonicalName();

	public static final String SERVICE_NAME = "SESSION_REPAIRE_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	private SessionBusiness sessionBusiness;
	private LocationBusiness locationBusiness;

	public SessionRepaireService() {
		super(SERVICE_NAME);

    	locationBusiness = LocationBusiness.getInstance(getContext(), getNotifier());
    	sessionBusiness = SessionBusiness.getInstance(getContext(), getNotifier());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");

		long idSession = intent.getLongExtra(INTENT_KEY_SESSION, -1);
		if (idSession>-1) {
			repaire(idSession);
		}
	}

	private void repaire(long idSession) {
		long nb = locationBusiness.countBySession(idSession);
    	if (nb>0) {
    		try {
    			Session session = sessionBusiness.getById(idSession);

    			/**
		    	 * Get Datas from locations
		    	 */
    			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_SESSION, R.string.repaire_session_message_session, -1, -1);
	    		repaireSession(session);
				repaireLocalisation(session);
		
		        /**
		         * Update Session
		         */
    			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_SESSION, R.string.repaire_session_message_save, -1, -1);
		        updateDatabase(session);
    		}
    		finally {
    			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_END, R.string.repaire_session_message_finished, -1, -1);
    		}
    	}
	}

	private void repaireSession(Session session) {
		long idSession = session.getId();
    	long idStart = locationBusiness.getMinIdWithAdresseBySession(idSession);
		if (idStart<0)
			idStart = locationBusiness.getMinIdBySession(idSession);
		if (session.getStart()==null || session.getStart().getId()!=idStart) {
			session.setStart(locationBusiness.getById(idStart));
//			locationBusiness.deleteBeforeIdByIdSession(idSession, idStart);
		}
		long idEnd = locationBusiness.getMaxIdWithAdresseBySession(idSession);
		if (idEnd<0)
			idEnd = locationBusiness.getMaxIdBySession(idSession);
		if (session.getEnd()==null || session.getEnd().getId()!=idEnd) {
			session.setEnd(locationBusiness.getById(idEnd));
//			locationBusiness.deleteAfterIdByIdSession(idSession, idEnd);
		}
		if (session.getTimeStart()==null) {
			long time = locationBusiness.getMinTimeBySession(idSession);
			if (time>0)
				session.setTimeStart(new Date(time));
		}
		if (session.getTimeStop()==null) {
			long time = locationBusiness.getMaxTimeBySession(idSession);
			if (time>0)
				session.setTimeStop(new Date(time));
		}
		if (session.getTimeSend()==null) {
			long time = locationBusiness.getMaxTimeSendBySession(idSession);
			if (time>0)
				session.setTimeSend(new Date(time));
		}
		session.setCalculateElapsedTime(
			session.getTimeStart()==null || session.getTimeStop()==null ? 0 : session.getTimeStop().getTime()-session.getTimeStart().getTime()
		);
	}

	private void repaireLocalisation(Session session) {
		notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_LOCATION, R.string.repaire_session_message_locations, -1, -1);
    	List<Localisation> listLocalisation = locationBusiness.getByIdSession(session.getId());

    	int nb = 0, total = listLocalisation.size();
    	int notifSize = (total*5)/100;
    	double distanceTotal = 0d;
		long elapsedTimeTotal = 0;
		Localisation previousLocalisation = null;
		for (Localisation localisation : listLocalisation) {
    		if ((nb%notifSize)==0)
    			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_LOCATION, R.string.repaire_session_message_locations_count, nb, total);

			double distance = 0.0d;
			long elapsedTime = 0;
    		float speed = 0.0f; 
    		float speedAverage = 0.0f; 

			if (previousLocalisation!=null) {
				distance = ToolCalculate.getDistance(
					ApplicationData.getInstance(this).getDistanceMethodeCalcul(),
					previousLocalisation.getLatitude(), previousLocalisation.getLongitude(), 
					localisation.getLatitude(), localisation.getLongitude(),
					previousLocalisation.getAltitude(), localisation.getAltitude()
				);
				elapsedTime = (previousLocalisation.getTime()>0  && localisation.getTime()>0) ? localisation.getTime() - previousLocalisation.getTime() : 0;

				distanceTotal += distance;
				elapsedTimeTotal += elapsedTime;
	    		speed = (elapsedTime<=0) ? 0.0f : (float) ((distance / elapsedTime) * 100000f); 
	    		speedAverage = (elapsedTimeTotal<=0) ? 0.0f : (float) ((distanceTotal / elapsedTimeTotal) * 100000f); 
			}

			localisation.setCalculateDistance(distance);
			localisation.setCalculateDistanceCumul(distanceTotal);
			localisation.setCalculateElapsedTime(elapsedTime);
			localisation.setCalculateElapsedTimeCumul(elapsedTimeTotal);
    		localisation.setCalculateSpeed(speed);
    		localisation.setCalculateSpeedAverage(speedAverage);
			locationBusiness.updateCalculate(localisation);

			previousLocalisation = localisation;

			nb++;
		}

		session.setCalculateDistance(distanceTotal);
		session.setCalculateElapsedTime(elapsedTimeTotal);
	}

	private void updateDatabase(Session session) {
    	sessionBusiness.updateAndCalculate(session);
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