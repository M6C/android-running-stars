package com.runningstars.activity.business;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Intent;
import android.content.IntentFilter;

import com.cameleon.common.factory.FactoryNotifier;
import com.runningstars.activity.GPSMapActivity;
import com.runningstars.activity.SessionDetailActivity;
import com.runningstars.activity.business.notifier.SessionDetailRepaireProgressNotifier;
import com.runningstars.business.LocationBusiness;
import com.runningstars.business.SessionBusiness;
import com.runningstars.service.session.SessionDeleteService;
import com.runningstars.service.session.SessionKmlService;
import com.runningstars.service.session.SessionRepaireService;
import com.runningstars.service.session.SessionSendService;
import com.runningstars.service.session.receiver.SessionProgressReceiver;
import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;

public class SessionDetailBusiness {
	private static final String TAG = SessionDetailBusiness.class.getCanonicalName();

	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	private SessionDetailActivity activity;

	private Session session;
	private Localisation start;
	private Localisation end;

	private long nb = 0;

	private SessionProgressReceiver sendSessionProgressReceiver;
	private SessionProgressReceiver repaireSessionProgressReceiver;

	public SessionDetailBusiness(SessionDetailActivity activity) {
		super();
		this.activity = activity;

		initData();
	}

	public SessionDetailActivity getContext() {
		return activity;
	}

	public Session getSession() {
		return session;
	}

	private void initData() {
		Intent indentActivity = getContext().getIntent();
		long idSession = indentActivity.getLongExtra(INTENT_KEY_SESSION, 0);
		initData(idSession);
	}

	private void initData(long id) {
		SessionBusiness sessionBusiness = SessionBusiness.getInstance(getContext(), activity);
    	LocationBusiness locationBusiness = LocationBusiness.getInstance(getContext(), activity);

    	session = sessionBusiness.getById(id);

    	if (session.getStart()!=null)
    		start = locationBusiness.getById(session.getStart().getId());
    	else
    		start = null;
    	if (session.getEnd()!=null)
    		end = locationBusiness.getById(session.getEnd().getId());
    	else
    		end = null;

    	session.setStart(start);
    	session.setEnd(end);

    	session.setPngMapScreenShoot(sessionBusiness.getPngMapScreenShoot(session));

    	nb = locationBusiness.countBySession(id);
	}

	public void onResume() {
		logMe("onResume");
		registerReceiver();
	}

	public void onPause() {
		logMe("onPause");
		unregisterReceiver();
	}

	public void onDestroy() {
		logMe("onDestroy");
	}

	public void delete() {
    	Intent intent = new Intent(getContext(), SessionDeleteService.class);
    	intent.putExtra(SessionDeleteService.INTENT_KEY_SESSION, session.getId());
    	getContext().startService(intent);
	}

	public void kml() {
    	Intent intent = new Intent(getContext(), SessionKmlService.class);
    	intent.putExtra(SessionKmlService.INTENT_KEY_SESSION, session.getId());
    	getContext().startService(intent);
	}

	public void repaire() {
    	Intent intent = new Intent(getContext(), SessionRepaireService.class);
    	intent.putExtra(SessionRepaireService.INTENT_KEY_SESSION, session.getId());
    	getContext().startService(intent);
	}

	public void send() {
    	Intent intent = new Intent(getContext(), SessionSendService.class);
    	intent.putExtra(SessionSendService.INTENT_KEY_SESSION, session.getId());
    	getContext().startService(intent);

        /**
        * reinit data and update ihm
        */
		reinitializDataAndUpdateIhm();
	}

	public void map() {
		Intent intent = new Intent(getContext(), GPSMapActivity.class);
		intent.putExtra("ID_SESSION", session.getId());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
	}

	
    /**
     * reinit data and update ihm
     */
	public void reinitializDataAndUpdateIhm() {
		// Update Data
		initData(session.getId());
		// Update IHM
		activity.iniIhm();
	}

	private INotifierSessionProgress getNotifierSendSessionProgress() {
		return FactoryNotifier.getInstance().getNotifierSendSessionProgress(activity);
	}

	private void registerReceiver() {
		logMe("registerReceiver");
		registerReceiverProgress();
		registerRepaireReceiverProgress();
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

	private void registerRepaireReceiverProgress() {
		logMe("registerRepaireReceiverProgress");
		if (repaireSessionProgressReceiver!=null) {
			logEr("repaireSessionProgressReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SessionProgressReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		repaireSessionProgressReceiver = new SessionProgressReceiver(new SessionDetailRepaireProgressNotifier(this));
		activity.registerReceiver(repaireSessionProgressReceiver, filter);
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		unregisterReceiverProgress();
		unregisterRepaireReceiverProgress();
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

	public void unregisterRepaireReceiverProgress() {
		logMe("unregisterRepaireReceiverProgress");
		if (repaireSessionProgressReceiver==null) {
			logEr("repaireSessionProgressReceiver already unregistred");
			return;
		}

		activity.unregisterReceiver(repaireSessionProgressReceiver);
		repaireSessionProgressReceiver = null;
	}

	public long getNb() {
		return nb;
	}

	/**
	 * @return the start
	 */
	public Localisation getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Localisation start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Localisation getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Localisation end) {
		this.end = end;
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
