package com.runningstars.activity.business;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

//import com.addon.api.graphics.ScreenShoot;
import com.cameleon.common.factory.FactoryNotifier;
import com.cameleon.common.tool.ToolDatetime;
import com.runningstars.ApplicationData;
import com.runningstars.activity.GPSMapActivity;
import com.runningstars.activity.SessionDetailActivity;
import com.runningstars.business.Business;
import com.runningstars.business.LocationBusiness;
import com.runningstars.business.SessionBusiness;
import com.runningstars.service.map.MapCalculateLocationService;
import com.runningstars.service.map.receiver.MapCalculateLocationReceiver;
import com.runningstars.service.map.receiver.inotifier.INotifierMapCalculateLocation;
import com.runningstars.service.session.receiver.SessionProgressReceiver;
import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;
import com.runningstars.tool.ToolCalculate;

public class GPSMapBusiness implements INotifierMapCalculateLocation {
	private static final String TAG = GPSMapBusiness.class.getCanonicalName();

	private GPSMapActivity activity;

	private SessionBusiness sessionBusiness;
	private LocationBusiness locationBusiness;
	private Business business;
	private ApplicationData applicationData;

	private Session session;


	private INotifierSessionProgress notifierProgress;

	private long idSession;
	private boolean saveScreenshoot;
	private int scale;
	private int bearing;

	private SessionProgressReceiver sendSessionProgressReceiver;

	private MapCalculateLocationReceiver mapCalculateLocationReceiver;

	public GPSMapBusiness(GPSMapActivity activity) {
		super();
		this.activity = activity;
		this.business = Business.getInstance(activity, activity);
		this.sessionBusiness = SessionBusiness.getInstance(activity, activity);
		this.locationBusiness = LocationBusiness.getInstance(activity, activity);
		this.applicationData = ApplicationData.getInstance(activity);
	}

	public void onCreate(Bundle savedInstanceState) {
		logMe("onCreate");
		Intent intent = getContext().getIntent();

		idSession = intent.getLongExtra("ID_SESSION", -1);
		saveScreenshoot = intent.getBooleanExtra("SAVE_SCREENSHOOT", false);
		scale = intent.getIntExtra("SCALE", ApplicationData.getInstance(getContext()).getMapScaleInMeter());
		bearing = intent.getIntExtra("BEARING", ApplicationData.getInstance(getContext()).getMapBearingScale());

		notifierProgress = getNotifierSendSessionProgress();
	}

	public void onResume() {
		logMe("onResume");
		registerReceiver();

		if (idSession >= 0) {
			session = business.getSessionWithLocation(idSession);
			String txtTitle = "";
			txtTitle += session.getTimeStart()!=null ? " Date:" + ToolDatetime.toDatetimeShort(session.getTimeStart()) : "";
			txtTitle += " Duration:" + ToolCalculate.formatElapsedTime(session);
			txtTitle += " Distance:" + ToolCalculate.formatDistance(session);
			txtTitle += " Speed:" + ToolCalculate.formatSpeedKmH(session);

			activity.setTitle(txtTitle);

			if (session.getStart()!=null) {
				session.setStart(locationBusiness.getById(session.getStart().getId()));
			}
			if (session.getEnd()!=null) {
				session.setEnd(locationBusiness.getById(session.getEnd().getId()));
			}

			startCalculateLocationService();
		}
	}

	public void onPause() {
		logMe("onPause");
		unregisterReceiver();
	}

	public void onDestroy() {
		logMe("onDestroy");
		if (session!=null) {
			Intent intent = new Intent(activity, SessionDetailActivity.class);
			intent.putExtra(SessionDetailBusiness.INTENT_KEY_SESSION, session.getId());
			activity.startActivity(intent);
		}
	}

	public Activity getContext() {
		return activity;
	}

	public void notifyLocation(List<Localisation> list) {
		activity.notifyLocation(session, list);

		if (saveScreenshoot) {
			saveMapScreenShoot();
		}
	}

	public void notifyLocation(Localisation localisation, Localisation previous) {
		activity.notifyLocation(localisation, previous);
	}

	public void updateScreenShoot() {
		saveMapScreenShoot();
	}

	private void startCalculateLocationService() {
		Intent intent = new Intent(getContext(), MapCalculateLocationService.class);
		intent.putExtra(MapCalculateLocationService.INTENT_KEY_SESSION, idSession);
		intent.putExtra(MapCalculateLocationService.INTENT_KEY_SCALE, scale);
		intent.putExtra(MapCalculateLocationService.INTENT_KEY_BEARING, bearing);
    	getContext().startService(intent);
	}


    private void saveMapScreenShoot() {
    	logMe("saveMapScreenShoot");
		try {
			// Android GingerBread 2.3
//			byte[] byteArray = ScreenShoot.toByteArray(activity.getMapView(), applicationData.getMapScreenShootQuality());
//			session.setPngMapScreenShoot(byteArray);
//			sessionBusiness.setPngMapScreenShoot(session);
		}
		catch (Exception ex) {
			logMe(ex);
		}
    }

	private INotifierSessionProgress getNotifierSendSessionProgress() {
		return FactoryNotifier.getInstance().getNotifierSendSessionProgress(activity);
	}

	private void registerReceiver() {
		logMe("registerReceiver");
		registerReceiverProgress();
		registerReceiverLocation();
	}

	private void unregisterReceiver() {
		logMe("unregisterReceiver");
		unregisterReceiverProgress();
		unregisterReceiverLocation();
	}

	private void registerReceiverProgress() {
		logMe("registerReceiverProgress");
		if (sendSessionProgressReceiver!=null) {
			logEr("sendSessionProgressReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(SessionProgressReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		sendSessionProgressReceiver = new SessionProgressReceiver(notifierProgress);
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

	private void registerReceiverLocation() {
		logMe("registerReceiverLocation");
		if (mapCalculateLocationReceiver!=null) {
			logEr("mapCalculateLocationReceiver already registred");
			return;
		}

		IntentFilter filter = new IntentFilter(MapCalculateLocationReceiver.BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		mapCalculateLocationReceiver = new MapCalculateLocationReceiver(this);
		activity.registerReceiver(mapCalculateLocationReceiver, filter);
	}

	private void unregisterReceiverLocation() {
		logMe("unregisterReceiverLocation");
		if (mapCalculateLocationReceiver==null) {
			logEr("mapCalculateLocationReceiver already unregistred");
			return;
		}

		activity.unregisterReceiver(mapCalculateLocationReceiver);
		mapCalculateLocationReceiver = null;
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
	 * 
	 * @param ex
	 */
	private void logMe(Exception ex) {
		logMe(ex.getMessage());
	}

    /**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
