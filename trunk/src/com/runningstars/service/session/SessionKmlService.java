package com.runningstars.service.session;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.runningstars.ApplicationData;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.business.SessionBusiness;
import com.runningstars.factory.FactoryNotifier;

public class SessionKmlService extends IntentService {
	private static final String TAG = SessionKmlService.class.getCanonicalName();

	public static final String SERVICE_NAME = "SESSION_KML_SERVICE"; 
	public static final String INTENT_KEY_SESSION = "ID_SESSION"; 

	private SessionBusiness sessionBusiness;

	public SessionKmlService() {
		super(SERVICE_NAME);

    	sessionBusiness = SessionBusiness.getInstance(getContext(), getNotifier());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logMe("onHandleIntent");

		long idSession = intent.getLongExtra(INTENT_KEY_SESSION, -1);
		if (idSession>-1) {
			kml(idSession);
		}
	}

	public void kml(long idSession) {
		Session session = sessionBusiness.getById(idSession);
		String strUrl = ApplicationData.getInstance(getContext()).getMysqlServerUrl();

		String lowUrl = strUrl.toLowerCase();
		if (!lowUrl.startsWith("http://") && !lowUrl.startsWith("https://"))
			strUrl = "http://" + strUrl;

		if (!lowUrl.endsWith("/"))
			strUrl += "/";

		strUrl += "get_kml_by_session.php?id_session=" + session.getIdSend();

        Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(strUrl), "application/vnd.google-earth.kml+xml");
		i.putExtra("id_session", idSession);
		getContext().startActivity(i);
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
