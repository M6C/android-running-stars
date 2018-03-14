package com.runningstars.business;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;

public class Business {
	
	private static Business instance;

	private DBSessionDataSource dbSessionDataSource;
	private DBLocationDataSource dbLocationDataSource;

	private Business(Context context, INotifierMessage notificationMessage) {
		dbSessionDataSource = new DBSessionDataSource(context, notificationMessage);
		dbLocationDataSource = new DBLocationDataSource(context, notificationMessage);
	}

	public static Business getInstance(Context context, INotifierMessage notificationMessage) {

		if (instance==null)
			instance = new Business(context, notificationMessage);

        return instance;
	}

	public List<Session> getSessionAllWithLocation() {
		List<Session> listSession = null;
		try {
	        dbSessionDataSource.open();
	        dbLocationDataSource.open();
			// Get in database
			listSession = dbSessionDataSource.getAll();
			for(Session session : listSession) {
				Localisation start = null, end = null;
				if (session.getStart()!=null) {
					start = dbLocationDataSource.getById(session.getStart().getId());
				}
				if (session.getEnd()!=null) {
					end = dbLocationDataSource.getById(session.getEnd().getId());
				}
				session.setStart(start);
				session.setEnd(end);
			}
		}
		finally {
	        dbLocationDataSource.close();
	        dbSessionDataSource.close();
		}
		return listSession;
	}

	public void updateSessionPngMapScreenShoot(List<Session> listSession) {
		try {
	        dbSessionDataSource.open();
			for(Session session : listSession) {
				byte[] buf = dbSessionDataSource.getPngMapScreenShootById(session);
				session.setPngMapScreenShoot(buf);
//				if (buf!=null) {
//					File path = Environment.getExternalStorageDirectory();
//					File file = new File(path, "map_db_" + session.getId() + ".png");
//					BufferedOutputStream outStream = null;
//					try {
//						outStream = new BufferedOutputStream(new FileOutputStream(file));
//						outStream.write(buf);
//						outStream.flush();
//					} catch (FileNotFoundException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					finally {
//						try {
//							if (outStream!=null) {
//								outStream.close();
//							}
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
			}
		}
		finally {
			dbSessionDataSource.close();
		}
	}

	public Session getSessionWithLocation(long id) {
		Session session = null;
		Localisation start = null, end = null;
		try {
	        dbSessionDataSource.open();
	        dbLocationDataSource.open();

	        // Get in database
			session = dbSessionDataSource.getById(id);
			if (session.getStart()!=null) {
				start = dbLocationDataSource.getById(session.getStart().getId());
			}
			if (session.getEnd()!=null) {
				end = dbLocationDataSource.getById(session.getEnd().getId());
			}
			session.setStart(start);
			session.setEnd(end);
		}
		finally {
	        dbLocationDataSource.close();
	        dbSessionDataSource.close();
		}
		return session;
	}
}
