package com.runningstars.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;

import com.runningstars.ApplicationData;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;
import com.runningstars.db.task.InsertSession;

public class SessionBusiness {
	
	private static SessionBusiness instance;

	private Context context;
	private INotifierMessage notificationMessage;

	private DBSessionDataSource dbSessionDataSource;

	private SessionBusiness(Context context, INotifierMessage notificationMessage) {
		this.context = context;
		this.notificationMessage = notificationMessage;

		dbSessionDataSource = new DBSessionDataSource(context, notificationMessage);
	}

	public static SessionBusiness getInstance(Context context, INotifierMessage notificationMessage) {

		if (instance==null)
			instance = new SessionBusiness(context, notificationMessage);

        return instance;
	}

	public void sendSession(final Session session) {
		/**
		 * Create SESSION
		 */
		String url = ApplicationData.getInstance(context).getMysqlServerUrl();

		List<Session> list = new ArrayList<Session>();
		list.add(session);
    	new InsertSession(context, notificationMessage, list, url).execute();
	}

    /**
     * 
     * @param session
     */
	public void create(Session session) {
    	try {
    		dbSessionDataSource.open();
	        session.setTimeStart(new Date());
			// Save in database
			dbSessionDataSource.create(session);
    	}
    	finally {
    		dbSessionDataSource.close();
    	}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public void update(Session session) {
		try {
			dbSessionDataSource.open();
			// Update in database
			dbSessionDataSource.update(session);
		}
		finally {
			dbSessionDataSource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public void updateAndCalculate(Session session) {
		try {
			dbSessionDataSource.open();
			// Update in database
			dbSessionDataSource.update(session);
			// Update Session calculate
			dbSessionDataSource.updateCalculate(session);
		}
		finally {
			dbSessionDataSource.close();
		}
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public void updateSend(Session session) {
		try {
			dbSessionDataSource.open();
			// Update in database
			dbSessionDataSource.updateSend(session);
		}
		finally {
			dbSessionDataSource.close();
		}
	}

    /**
     * 
     * @param session
     */
    public void updateCalculate(Session session) {
		try {
			dbSessionDataSource.open();
			dbSessionDataSource.updateCalculate(session);
		}
		finally {
			dbSessionDataSource.close();
		}
    }

    /**
     * 
     * @param session
     */
    public byte[] getPngMapScreenShoot(Session session) {
		try {
			dbSessionDataSource.open();
			return dbSessionDataSource.getPngMapScreenShootById(session);
		}
		finally {
			dbSessionDataSource.close();
		}
    }

    /**
     * 
     * @param session
     */
    public void setPngMapScreenShoot(Session session) {
		try {
			dbSessionDataSource.open();
			dbSessionDataSource.setPngMapScreenShoot(session);
		}
		finally {
			dbSessionDataSource.close();
		}
    }

	/**
	 * 
	 * @param idSession
	 * @return
	 */
    public Session getById(long idSession) {
		try {
			dbSessionDataSource.open();
			// Update in database
			return dbSessionDataSource.getById(idSession);
		}
		finally {
			dbSessionDataSource.close();
		}
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
    public Session getPrevious(Session session) {
		try {
			dbSessionDataSource.open();
			// Get in database
			return dbSessionDataSource.getPrevious(session);
		}
		finally {
			dbSessionDataSource.close();
		}
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
    public Session getNext(Session session) {
		try {
			dbSessionDataSource.open();
			// Get in database
			return dbSessionDataSource.getNext(session);
		}
		finally {
			dbSessionDataSource.close();
		}
	}
}
