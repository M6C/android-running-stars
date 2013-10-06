package com.runningstars.db.sqlite.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.helper.DBLocationHelper;

public class DBLocationDataSource {

	private static final String TAG = DBLocationDataSource.class.getCanonicalName();

	private SQLiteDatabase db;
	private DBLocationHelper dbHelper;

	// Database fields
	private String[] allColumns = {
		DBLocationHelper.COLUMN_ID,
		DBLocationHelper.COLUMN_ID_SESSION, DBLocationHelper.COLUMN_SPEED,
		DBLocationHelper.COLUMN_LONGITUDE,
		DBLocationHelper.COLUMN_LATITUDE, DBLocationHelper.COLUMN_ALTITUDE,
		DBLocationHelper.COLUMN_ACCURACY, DBLocationHelper.COLUMN_BEARING,
		DBLocationHelper.COLUMN_TIME, DBLocationHelper.COLUMN_ADDRESSLINE,
		DBLocationHelper.COLUMN_POSTALCODE,
		DBLocationHelper.COLUMN_LOCALITY, DBLocationHelper.COLUMN_TIME_SEND,
		DBLocationHelper.COLUMN_CAL_DISTANCE, DBLocationHelper.COLUMN_CAL_DISTANCE_CUMUL,
		DBLocationHelper.COLUMN_CAL_ELAPSED_TIME, DBLocationHelper.COLUMN_CAL_ELAPSED_TIME_CUMUL
	};

	private INotifierMessage notificationMessage;

	public DBLocationDataSource(Context context, INotifierMessage notificationMessage) {
		this.notificationMessage = notificationMessage;
		dbHelper = new DBLocationHelper(context, notificationMessage);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		/**
		 * Close Location DB
		 */
		try {
			dbHelper.close();
		}
		catch(Exception ex) {
			notificationMessage.notifyError(ex);
		}
	}

	public void recreateTable() {
		dbHelper.recreateTable(db);
	}

	public Localisation create(Localisation localisation) {
		Date dateStart = new Date();
		ContentValues values = new ContentValues();

		values.put(DBLocationHelper.COLUMN_ID_SESSION, localisation.getSession().getId());
		values.put(DBLocationHelper.COLUMN_SPEED, localisation.getSpeed());
		values.put(DBLocationHelper.COLUMN_LONGITUDE, localisation.getLongitude());
		values.put(DBLocationHelper.COLUMN_LATITUDE, localisation.getLatitude());
		values.put(DBLocationHelper.COLUMN_ALTITUDE, localisation.getAltitude());
		values.put(DBLocationHelper.COLUMN_ACCURACY, localisation.getAccuracy());
		values.put(DBLocationHelper.COLUMN_BEARING, localisation.getBearing());
		values.put(DBLocationHelper.COLUMN_TIME, localisation.getTime());
		values.put(DBLocationHelper.COLUMN_ADDRESSLINE, localisation.getAddressLine());
		values.put(DBLocationHelper.COLUMN_POSTALCODE, localisation.getPostalCode());
		values.put(DBLocationHelper.COLUMN_LOCALITY, localisation.getLocality());
		values.put(DBLocationHelper.COLUMN_CAL_DISTANCE, localisation.getCalculateDistance());
		values.put(DBLocationHelper.COLUMN_CAL_DISTANCE_CUMUL, localisation.getCalculateDistanceCumul());
		values.put(DBLocationHelper.COLUMN_CAL_ELAPSED_TIME, localisation.getCalculateElapsedTime());
		values.put(DBLocationHelper.COLUMN_CAL_ELAPSED_TIME_CUMUL, localisation.getCalculateElapsedTimeCumul());

		localisation.setId(db.insert(DBLocationHelper.TABLE_NAME, null, values));

		logMe("create(localisation.id:" + localisation.getId() + ")", dateStart);
		return localisation;
	}

	public void delete(Localisation localisation) {
		Date dateStart = new Date();
		long id = localisation.getId();
		logMe("Localisation deleted with id: " + id);
		db.delete(DBLocationHelper.TABLE_NAME, DBLocationHelper.COLUMN_ID + " = " + id, null);
		logMe("session(localisation.id:" + localisation.getId() + ")", dateStart);
	}

	public void delete(Session session) {
		Date dateStart = new Date();
		long id = session.getId();
		logMe("Localisation deleted with id_session: " + id);
		db.delete(DBLocationHelper.TABLE_NAME, DBLocationHelper.COLUMN_ID_SESSION + " = " + id, null);
		logMe("delete(session.id:" + session.getId() + ")", dateStart);
	}

	public void updateTimeSend(Localisation localisation) {
		Date dateStart = new Date();
		long id = localisation.getId();
		long timeSend = localisation.getTimeSend();

		logMe("Localisation updateTimeSend with id: " + id + " and timeSend: " + timeSend);
		ContentValues values = new ContentValues();
		values.put(DBLocationHelper.COLUMN_TIME_SEND, timeSend);

		db.update(DBLocationHelper.TABLE_NAME, values, DBLocationHelper.COLUMN_ID + " = " + id, null);
		logMe("updateTimeSend(localisation.id:" + localisation.getId() + ")", dateStart);
	}

	public void updateCalculate(Localisation localisation) {
		Date dateStart = new Date();
		long id = localisation.getId();

		logMe("Localisation updateCalculate with id: " + id);
		ContentValues values = new ContentValues();
		values.put(DBLocationHelper.COLUMN_CAL_DISTANCE, localisation.getCalculateDistance());
		values.put(DBLocationHelper.COLUMN_CAL_DISTANCE_CUMUL, localisation.getCalculateDistanceCumul());
		values.put(DBLocationHelper.COLUMN_CAL_ELAPSED_TIME, localisation.getCalculateElapsedTime());
		values.put(DBLocationHelper.COLUMN_CAL_ELAPSED_TIME_CUMUL, localisation.getCalculateElapsedTimeCumul());

		db.update(DBLocationHelper.TABLE_NAME, values, DBLocationHelper.COLUMN_ID + " = " + id, null);
		logMe("updateCalculate(localisation.id:" + localisation.getId() + ")", dateStart);
	}

	public void updateTimeSendBySession(Session session) {
		updateTimeSendBySession(session, (List<Localisation>)null);
	}

	public void updateTimeSendBySession(Session session, Date dateSend) {
		updateTimeSendBySession(session, null, dateSend);
	}

	public void updateTimeSendBySession(Session session, List<Localisation> listLocalisation) {
		updateTimeSendBySession(session, listLocalisation, new Date());
	}

	public void updateTimeSendBySession(Session session, List<Localisation> listLocalisation, Date dateSend) {
		Date dateStart = new Date();
		long id = session.getId();
		long timeSend = (dateSend==null) ? 0 : dateSend.getTime();

		logMe("Localisation updateTimeSend with id: " + id + " and timeSend: " + timeSend);
		ContentValues values = new ContentValues();
		values.put(DBLocationHelper.COLUMN_TIME_SEND, timeSend);

		String sqlWhere = DBLocationHelper.COLUMN_ID_SESSION + " = " + id;
//		sqlWhere += " AND " +	DBLocationHelper.COLUMN_TIME_SEND + " IS NULL ";
		if (listLocalisation!=null && !listLocalisation.isEmpty()) {
			String sqlIn = "";
			for(Localisation localisation : listLocalisation) {
				if (!"".equals(sqlIn))
					sqlIn += ",";
				sqlIn += localisation.getId();
			}
			sqlWhere += " AND " + DBLocationHelper.COLUMN_ID + " IN (" + sqlIn + ") ";
		}
		db.update(DBLocationHelper.TABLE_NAME, values, sqlWhere, null);
		logMe("updateTimeSendBySession(session.id:" + session.getId() + ", listLocalisation.size:" + (listLocalisation==null ? 0 : listLocalisation.size()) + ")", dateStart);
	}

	public Localisation getById(long id) {
		Date dateStart = new Date();
		Localisation ret = null;
		Cursor cursor = db.query(DBLocationHelper.TABLE_NAME,
				allColumns, DBLocationHelper.COLUMN_ID + " = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			ret = cursorToLocalisation(cursor);
		}
		cursor.close();
		logMe("getById(id:" + id + ")", dateStart);
		return ret;
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMinIdBySession(long idSession) {
		String cal = calculateBySession("MIN", DBLocationHelper.COLUMN_ID, idSession);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMaxIdBySession(long idSession) {
		String cal = calculateBySession("MAX", DBLocationHelper.COLUMN_ID, idSession);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMinIdWithAdresseBySession(long idSession) {
		String andWhere = " AND " + DBLocationHelper.COLUMN_ADDRESSLINE + " is not null";
		String cal = calculateBySession("MIN", DBLocationHelper.COLUMN_ID, idSession, andWhere);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMaxIdWithAdresseBySession(long idSession) {
		String andWhere = " AND " + DBLocationHelper.COLUMN_ADDRESSLINE + " is not null";
		String cal = calculateBySession("MAX", DBLocationHelper.COLUMN_ID, idSession, andWhere);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMinTimeBySession(long idSession) {
		String cal = calculateBySession("MIN", DBLocationHelper.COLUMN_TIME, idSession);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMaxTimeBySession(long idSession) {
		String cal = calculateBySession("MAX", DBLocationHelper.COLUMN_TIME, idSession);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long getMaxTimeSendBySession(long idSession) {
		String cal = calculateBySession("MAX", DBLocationHelper.COLUMN_TIME_SEND, idSession);
		return (cal==null) ? -1 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public double getSumDistanceBySession(long idSession) {
		String cal = calculateBySession("SUM", DBLocationHelper.COLUMN_CAL_DISTANCE, idSession);
		return (cal==null) ? 0 : Double.parseDouble(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public long countBySession(long idSession) {
		String cal = calculateBySession("COUNT", DBLocationHelper.COLUMN_ID, idSession);
		return (cal==null) ? 0 : Long.parseLong(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public double sumDistanceBySession(long idSession) {
		String cal = calculateBySession("SUM", DBLocationHelper.COLUMN_CAL_DISTANCE, idSession);
		return (cal==null) ? 0d : Double.parseDouble(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	public double sumBearingBySession(long idSession) {
		String cal = calculateBySession("SUM", DBLocationHelper.COLUMN_BEARING, idSession);
		return (cal==null) ? 0d : Double.parseDouble(cal);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	private String calculateBySession(String calculate, String column, long idSession) {
		return calculateBySession(calculate, column, idSession, null);
	}

	/**
	 * 
	 * @param idSession
	 * @return
	 */
	private String calculateBySession(String calculate, String column, long idSession, String andWhere) {
		Date dateStart = new Date();
		String ret = null;

		String sql = "SELECT "+calculate+"("+column+") FROM " + DBLocationHelper.TABLE_NAME + 
				" WHERE " + DBLocationHelper.COLUMN_ID_SESSION + " = " + idSession + " ";
		if (andWhere != null)
			sql += andWhere;

		Cursor cursor = db.rawQuery(sql, null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			ret = cursor.getString(0);
		}
		// Make sure to close the cursor
		cursor.close();

		logMe("calculateBySession(calculate:" + calculate + ", column:" + column + ", idSession:" + idSession + ", andWhere:" + andWhere + ")", dateStart);
		return ret;
	}

	/**
	 * 
	 * @param idSession
	 * @param scaleInMeter
	 * @return
	 */
	public List<Localisation> getSumCumulateDistanceBySession(long idSession, int scaleInMeter) {
		Date dateStart = new Date();

		List<Localisation> localisations = new ArrayList<Localisation>();

		String sqlScaled = "SELECT " + 
				"MAX(" + DBLocationHelper.COLUMN_ID + ") AS SCALED_ID, " +
				"round(round(" + DBLocationHelper.COLUMN_CAL_DISTANCE_CUMUL + " * 1000) / " + scaleInMeter + ") AS SCALED_DISTANCE " + 
				" FROM " + DBLocationHelper.TABLE_NAME + " " +
				" WHERE " + DBLocationHelper.COLUMN_ID_SESSION + " = " + idSession +
				" GROUP BY SCALED_DISTANCE ";

		String sqlWhere = DBLocationHelper.COLUMN_ID + " IN (SELECT SCALED_ID FROM (" + sqlScaled + ") AS SCALED_LOCATION)";

		Cursor cursor = db.query(DBLocationHelper.TABLE_NAME, allColumns, 
				sqlWhere, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Localisation localisation = cursorToLocalisation(cursor);
			localisations.add(localisation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		logMe("getSumCumulateDistanceBySession(idSession:" + idSession + ", scaleInMeter:" + scaleInMeter + ")", dateStart);
		return localisations;
	}

	public List<Localisation> getNotSendByIdSession(long id) {
		return getNotSendByIdSession(id, -1, -1);
	}


	public List<Localisation> getNotSendByIdSession(long id, long idLocationStart, long idLocationEnd) {
		Date dateStart = new Date();
		List<Localisation> localisations = new ArrayList<Localisation>();
		String sqlWhere = DBLocationHelper.COLUMN_ID_SESSION + " = " + id;
//		sqlWhere += " AND " + DBLocationHelper.COLUMN_TIME_SEND + " IS NULL ";

		if (idLocationStart>=0)
			sqlWhere += " AND " + DBLocationHelper.COLUMN_ID + " >= " + idLocationStart;
		if (idLocationEnd>=0)
			sqlWhere += " AND " + DBLocationHelper.COLUMN_ID + " <= " + idLocationEnd;

		Cursor cursor = db.query(DBLocationHelper.TABLE_NAME, allColumns, 
				sqlWhere, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Localisation localisation = cursorToLocalisation(cursor);
			localisations.add(localisation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		logMe("getNotSendByIdSession(id:" + id + ", idLocationStart:" + idLocationStart + ", idLocationEnd:" + idLocationEnd + ")", dateStart);
		return localisations;
	}

	/**
	 * Return all Location
	 * @return Location list
	 */
	public List<Localisation> getAll() {
		Date dateStart = new Date();
		List<Localisation> localisations = new ArrayList<Localisation>();

		Cursor cursor = db.query(DBLocationHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Localisation localisation = cursorToLocalisation(cursor);
			localisations.add(localisation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		logMe("getAll()", dateStart);
		return localisations;
	}

	/**
	 * Return all Location for a Session
	 * @param idSession Session id
	 * @return Location list
	 */
	public List<Localisation> getByIdSession(long idSession) {
		Date dateStart = new Date();
		List<Localisation> localisations = new ArrayList<Localisation>();
		String sqlWhere = DBLocationHelper.COLUMN_ID_SESSION + " = " + idSession;

		Cursor cursor = db.query(DBLocationHelper.TABLE_NAME, allColumns, 
				sqlWhere, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Localisation localisation = cursorToLocalisation(cursor);
			localisations.add(localisation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		logMe("getByIdSession(idSession:" + idSession + ")", dateStart);
		return localisations;
	}

	/**
	 * 
	 * @param idSession
	 * @param id
	 */
	public void deleteBeforeIdByIdSession(long idSession, long id) {
		Date dateStart = new Date();
		String sqlWhere = DBLocationHelper.COLUMN_ID_SESSION + " = " + idSession + 
				" AND " + DBLocationHelper.COLUMN_ID + "<" + id;
		db.delete(DBLocationHelper.TABLE_NAME, sqlWhere, null);

		logMe("deleteBeforeIdByIdSession(idSession:" + idSession + ", id:" + id + ")", dateStart);
	}

	/**
	 * 
	 * @param idSession
	 * @param id
	 */
	public void deleteAfterIdByIdSession(long idSession, long id) {
		Date dateStart = new Date();
		String sqlWhere = DBLocationHelper.COLUMN_ID_SESSION + " = " + idSession + 
				" AND " + DBLocationHelper.COLUMN_ID + ">" + id;
		db.delete(DBLocationHelper.TABLE_NAME, sqlWhere, null);

		logMe("deleteAfterIdByIdSession(idSession:" + idSession + ", id:" + id + ")", dateStart);
	}

	private Localisation cursorToLocalisation(Cursor cursor) {
		int col = 0;
		Localisation localisation = new Localisation();
		localisation.setId(DbTool.getInstance().toLong(cursor, col++));
		localisation.setSession(new Session(DbTool.getInstance().toLong(cursor, col++)));
		localisation.setSpeed(cursor.getFloat(col++));
		localisation.setLongitude(cursor.getDouble(col++));
		localisation.setLatitude(cursor.getDouble(col++));
		localisation.setAltitude(cursor.getDouble(col++));
		localisation.setAccuracy(cursor.getFloat(col++));
		localisation.setBearing(cursor.getFloat(col++));
		localisation.setTime(DbTool.getInstance().toLong(cursor, col++));
		localisation.setAddressLine(cursor.getString(col++));
		localisation.setPostalCode(cursor.getString(col++));
		localisation.setLocality(cursor.getString(col++));
		localisation.setTimeSend(DbTool.getInstance().toLong(cursor, col++));
		localisation.setCalculateDistance(cursor.getDouble(col++));
		localisation.setCalculateDistanceCumul(cursor.getDouble(col++));
		localisation.setCalculateElapsedTime(DbTool.getInstance().toLong(cursor, col++));
		localisation.setCalculateElapsedTimeCumul(DbTool.getInstance().toLong(cursor, col++));
		return localisation;
	}

	public void backupDbToSdcard() {
		try {
			dbHelper.backupDbToSdcard();
	    } catch (Exception ex) {
	    	notificationMessage.notifyError(ex);
	    }
	}

	public void restoreDbFromSdcard() {
		try {
			dbHelper.restoreDbFromSdcard();
	    } catch (Exception ex) {
	    	notificationMessage.notifyError(ex);
	    }
	}

	private void logMe(String msg, Date dateStart) {
		logMe("DB Execution time:" + (new Date().getTime() - dateStart.getTime()) + "millisecond - " + msg);
    }

	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}