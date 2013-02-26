package com.runningstars.service.map;

import java.util.ArrayList;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.ApplicationData.METHODE_CALCUL_DISTANCE;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.business.Business;
import com.runningstars.business.LocationBusiness;
import com.runningstars.factory.FactoryNotifier;
import com.runningstars.service.map.receiver.MapCalculateLocationReceiver;
import com.runningstars.service.session.receiver.SessionProgressReceiver;
import com.runningstars.tool.ToolCalculate;

public class MapCalculateLocationService extends IntentService {
	private static final String TAG = MapCalculateLocationService.class.getCanonicalName();

	public static final String SERVICE_NAME = "MAP_CALCULATE_LOCATION_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 
	public static final String INTENT_KEY_SCALE = "ID_SCALE"; 
	public static final String INTENT_KEY_BEARING = "ID_BEARING";

	private LocationBusiness locationBusiness;
	private Business business;

	private Session session;

	private long idSession;
	private int scale;
	private int bearing;


	public MapCalculateLocationService() {
		super(SERVICE_NAME);

		this.business = Business.getInstance(getContext(), getNotifier());
    	this.locationBusiness = LocationBusiness.getInstance(getContext(), getNotifier());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");

		idSession = intent.getLongExtra(INTENT_KEY_SESSION, -1);
		scale = intent.getIntExtra(INTENT_KEY_SCALE, -1);
		bearing = intent.getIntExtra(INTENT_KEY_BEARING, -1);

		if (idSession>-1) {
			calculate();
		}
	}

	private void calculate() {
		session = business.getSessionWithLocation(idSession);

		ArrayList<Localisation> list = getSumCumulateBySession(idSession);
		if (list.size()>0) {
			calculateLocation(session, list);
			notifyBroadcastLocation(list);
		}
	}

    private void calculateLocation(Session session, List<Localisation> listLocalisation) {
    	Localisation previous = session.getStart();
    	for(Localisation localisation : listLocalisation) {
    		if (previous != null) {
    			localisation.setSpeedKmH(ToolCalculate.getDiffTimeToKmH(
    				ApplicationData.getInstance(this).getDistanceMethodeCalcul(),
    				previous, localisation
    			));
    		}
    		previous = localisation;
    	}
    }

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    private ArrayList<Localisation> getSumCumulateBySession(long idSession) {
    	logMe("getSumCumulateBySession");

    	List<Localisation> ret = null;

    	String msg = "Calculate result";

    	double dBearing = locationBusiness.sumBearingBySession(idSession);

    	if (scale==0 && (bearing==0.0d || dBearing==0.0d)) {
    		scale = 100;
    		msg += " default";
    	}
    	
    	if (scale>0) {
			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_SESSION, R.string.map_session_message_location_scale, -1, -1);
			
			int scaleInMeter = (int)((int)((session.getCalculateDistance() * 1000) / 50) / 10);
			scaleInMeter = (scaleInMeter<=0) ? 100 : scaleInMeter * 100; 

			msg += " scale: " + scale + "("+scaleInMeter+"M"+")";
	
			ret = locationBusiness.sumCumulateDistanceBySession(idSession, scaleInMeter);
    	}
    	else {
			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_SESSION, R.string.map_session_message_location_all, -1, -1);

			msg += " All";

			ret = locationBusiness.getByIdSession(idSession);
    	}

		if (bearing>0) {
			if (dBearing>0.0d) {
				msg += " bearing:"+bearing;
		    	ret = sumCumulateBearingBySession(ret, bearing);
			}
		}

		notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_END, R.string.map_session_message_locations_finish, -1, -1);

		msg += " (" + (ret==null ? 0 : ret.size()) + ")";

		getNotifier().notifyMessage(msg);

		return new ArrayList<Localisation>(ret);
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
    private List<Localisation> sumCumulateBearingBySession(List<Localisation> list, float bearingScale) {
    	Localisation previous = null;

    	int total = list.size();
    	if (total==0)
    		return new ArrayList<Localisation>();

    	int notifSize = (total*10)/100;
    	if (notifSize==0)
    		notifSize=1;

    	ArrayList<Localisation> ret = new ArrayList<Localisation>(notifSize*2);
    	for(int nb=0 ; nb<total ; nb++) {
    		if ((nb%notifSize)==0)
    			notifyBroadcastProgress(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE_LOCATION, R.string.map_session_message_locations_calculate_bearing, nb, total);
    		Localisation localisation = list.get(nb);
    		if (previous!=null) {
    			float bearing = Math.abs(localisation.getBearing() - previous.getBearing());
    			if (bearing>=bearingScale) {
    				notifyBroadcastLocation(localisation, previous);

    				ret.add(localisation);
        			previous = localisation;
    			}
    		}
    		else {
    			previous = localisation;
    		}
    	}
    	ret.trimToSize();

    	return ret;
	}

//  private List<Localisation> getSumCumulateBySession(List<Localisation> list, int scaleInMeter) {
//  	List<Localisation> ret = new ArrayList<Localisation>(); 
//
//  	float distRound = 0.0f, previousDistRound = 0.0f;
//  	for(Localisation localisation : list) {
//			distRound = Math.round(Math.round(localisation.getCalculateDistanceCumul()* 1000) / scaleInMeter);
//  		if (distRound!=previousDistRound) {
//				ret.add(localisation);
//  		}
//  		previousDistRound = distRound;
//  	}
//
//  	return ret;
//  }

	private void notifyBroadcastProgress(int progress, int messageId, int nb, int total) {
		Intent intent = new Intent(SessionProgressReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_PROGRESS_TYPE, progress);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_MESSAGE, messageId);
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_COUNT, nb); 
		intent.putExtra(SessionProgressReceiver.INTENT_KEY_TOTAL, total); 
		sendBroadcast(intent);
	}

	private void notifyBroadcastLocation(ArrayList<Localisation> list) {
		Intent intent = new Intent(MapCalculateLocationReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putParcelableArrayListExtra(MapCalculateLocationReceiver.INTENT_KEY_LIST_LOCATION, list); 
		sendBroadcast(intent);
	}

	private void notifyBroadcastLocation(Localisation localisation, Localisation previous) {
		Intent intent = new Intent(MapCalculateLocationReceiver.BROADCAST_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(MapCalculateLocationReceiver.INTENT_KEY_LOCATION, localisation); 
		intent.putExtra(MapCalculateLocationReceiver.INTENT_KEY_LOCATION_PREVIOUS, previous); 
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