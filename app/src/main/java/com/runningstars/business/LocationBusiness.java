package com.runningstars.business;

import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.ApplicationData;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.db.task.InsertLocation;

public class LocationBusiness {
	
	private static LocationBusiness instance;

	private Context context;
	private INotifierMessage notificationMessage;

	private DBLocationDataSource dbLocationDatasource;

	private LocationBusiness(Context context, INotifierMessage notificationMessage) {

		this.context = context;
		this.notificationMessage = notificationMessage;

		dbLocationDatasource = new DBLocationDataSource(context, notificationMessage);
	}

	public static LocationBusiness getInstance(Context context, INotifierMessage notificationMessage) {

		if (instance==null)
			instance = new LocationBusiness(context, notificationMessage);

        return instance;
	}

	public void sendLocation(final Session session) {

		/**
		 * Create LOCATION
		 */

		long idSession = session.getId();
		long iStart = getMinIdBySession(idSession);
		long iEnd = getMaxIdBySession(idSession);
		int iStep = 25;

		for(long i=iStart ; i<=iEnd ; i+=iStep) {
			sendLocation(session, i, i+iStep-1);
		}
	}

	public void sendLocation(final Session session, long iStart, long iEnd) {

		/**
		 * Create LOCATION
		 */
    	final List<Localisation> listLocation = getNotSendByIdSession(session, iStart, iEnd);
		if (listLocation!=null && listLocation.size()>0) {
			String url = ApplicationData.getInstance(context).getMysqlServerUrl();

        	new InsertLocation(context, notificationMessage, listLocation, session, url).execute();
		}
	}

	public Localisation getById(long id) {
		if (id<0)
			return null;

		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getById(id);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

	public List<Localisation> getNotSendByIdSession(Session session, long iStart, long iEnd) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getNotSendByIdSession(session.getId(), iStart, iEnd);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

    public void updateTimeSend(List<Localisation> listLocalisation) {
		try {
	        dbLocationDatasource.open();
			Session session = listLocalisation.get(0).getSession();
	    	dbLocationDatasource.updateTimeSendBySession(session, listLocalisation);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

    public void updateTimeSend(Session session) {
		try {
	        dbLocationDatasource.open();
	    	dbLocationDatasource.updateTimeSendBySession(session);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

    public void updateTimeSend(Session session, Date dateSend) {
		try {
	        dbLocationDatasource.open();
	    	dbLocationDatasource.updateTimeSendBySession(session, null, dateSend);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

    public void updateCalculate(Localisation localisation) {
		try {
	        dbLocationDatasource.open();
	    	dbLocationDatasource.updateCalculate(localisation);
		}
		finally {
	        dbLocationDatasource.close();
		}
    }

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMinIdWithAdresseBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMinIdWithAdresseBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMaxIdWithAdresseBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMaxIdWithAdresseBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMinIdBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMinIdBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMaxIdBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMaxIdBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMinTimeBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMinTimeBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMaxTimeBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMaxTimeBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long getMaxTimeSendBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getMaxTimeSendBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public long countBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.countBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public double sumDistanceBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.sumDistanceBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public double sumBearingBySession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.sumBearingBySession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public List<Localisation> getByIdSession(long idSession) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getByIdSession(idSession);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public void deleteBeforeIdByIdSession(long idSession, long id) {
		try {
	        dbLocationDatasource.open();
			// Delete in database
			dbLocationDatasource.deleteBeforeIdByIdSession(idSession, id);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public void deleteAfterIdByIdSession(long idSession, long id) {
		try {
	        dbLocationDatasource.open();
			// Delete in database
			dbLocationDatasource.deleteAfterIdByIdSession(idSession, id);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @param scaleInMeter
	 * @return
	 */
    public List<Localisation> sumCumulateDistanceBySession(long idSession, int scaleInMeter) {
		try {
	        dbLocationDatasource.open();
			// Get in database
			return dbLocationDatasource.getSumCumulateDistanceBySession(idSession, scaleInMeter);
		}
		finally {
	        dbLocationDatasource.close();
		}
	}
}
