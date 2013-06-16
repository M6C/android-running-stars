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

import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.helper.DBSessionHelper;

public class DBSessionDataSource {

	private static final String TAG = DBSessionDataSource.class.getCanonicalName();

	private SQLiteDatabase db;
	private DBSessionHelper dbHelper;

	// Database fields
	private String[] allColumns = {
		DBSessionHelper.COLUMN_ID,
		DBSessionHelper.COLUMN_ID_SEND,
		DBSessionHelper.COLUMN_LOCATION_START,
		DBSessionHelper.COLUMN_LOCATION_END,
		DBSessionHelper.COLUMN_TIME_START,
		DBSessionHelper.COLUMN_TIME_STOP,
		DBSessionHelper.COLUMN_TIME_SEND,
		DBSessionHelper.COLUMN_CAL_DISTANCE,
		DBSessionHelper.COLUMN_CAL_ELAPSED_TIME
	};

	private INotifierMessage notificationMessage;

	public DBSessionDataSource(Context context, INotifierMessage notificationMessage) {
		this.notificationMessage = notificationMessage;
		dbHelper = new DBSessionHelper(context, notificationMessage);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		/**
		 * Close Session DB
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

	public Session create(Session session) {
		Date dateStart = new Date();
		ContentValues values = new ContentValues();

		values.put(DBSessionHelper.COLUMN_LOCATION_START, (session.getStart() != null) ? session.getStart().getId() : null);
		values.put(DBSessionHelper.COLUMN_LOCATION_END, (session.getEnd() != null) ? session.getEnd().getId() : null);
		values.put(DBSessionHelper.COLUMN_TIME_START, session.getTimeStart().getTime());
		values.put(DBSessionHelper.COLUMN_CAL_DISTANCE, session.getCalculateDistance());
		values.put(DBSessionHelper.COLUMN_CAL_ELAPSED_TIME, session.getCalculateElapsedTime());

		session.setId(db.insert(DBSessionHelper.TABLE_NAME, null, values));

		logMe("create(session.id:" + session.getId() + ")", dateStart);
		return session;
	}

	public void update(Session session) {
		Date dateStart = new Date();
		ContentValues values = new ContentValues();

		values.put(DBSessionHelper.COLUMN_LOCATION_START, (session.getStart() != null) ? session.getStart().getId() : null);
		values.put(DBSessionHelper.COLUMN_LOCATION_END, (session.getEnd() != null) ? session.getEnd().getId() : null);
		values.put(DBSessionHelper.COLUMN_TIME_START, (session.getTimeStart() != null) ? session.getTimeStart().getTime() : null);
		values.put(DBSessionHelper.COLUMN_TIME_STOP, (session.getTimeStop() != null) ? session.getTimeStop().getTime() : null);
		values.put(DBSessionHelper.COLUMN_TIME_SEND, (session.getTimeSend() != null) ? session.getTimeSend().getTime() : null);

		db.update(DBSessionHelper.TABLE_NAME, values, DBSessionHelper.COLUMN_ID + " = " + session.getId(), null);
		logMe("update(session.id:" + session.getId() + ")", dateStart);
	}

	public void updateSend(Session session) {
		Date dateStart = new Date();
		long id = session.getId();
		long id_send = session.getIdSend();
		long timeSend = (session.getTimeSend()!=null ? session.getTimeSend() : new Date()).getTime();

		logMe("Session updateTimeSend with id: " + id + " - id_send: " + id_send + " and timeSend: " + timeSend);
		ContentValues values = new ContentValues();
		values.put(DBSessionHelper.COLUMN_ID_SEND, id_send);
		values.put(DBSessionHelper.COLUMN_TIME_SEND, timeSend);

		db.update(DBSessionHelper.TABLE_NAME, values, DBSessionHelper.COLUMN_ID + " = " + id, null);
		logMe("updateSend(session.id:" + session.getId() + ")", dateStart);
	}

	public void updateCalculate(Session session) {
		Date dateStart = new Date();
		long id = session.getId();

		logMe("Session updateCalculate with id: " + id);
		ContentValues values = new ContentValues();
		values.put(DBSessionHelper.COLUMN_CAL_DISTANCE, session.getCalculateDistance());
//		values.put(DBSessionHelper.COLUMN_CAL_ELAPSED_TIME, session.getCalculateElapsedTime());
		values.put(DBSessionHelper.COLUMN_CAL_ELAPSED_TIME, session.getTimeStart()==null || session.getTimeStop()==null ? 0 : session.getTimeStop().getTime()-session.getTimeStart().getTime());

		db.update(DBSessionHelper.TABLE_NAME, values, DBSessionHelper.COLUMN_ID + " = " + id, null);
		logMe("updateCalculate(session.id:" + session.getId() + ")", dateStart);
	}

	public byte[] getPngMapScreenShootById(Session session) {
		Date dateStart = new Date();

		long id = session.getId();
		String[] columns = {
			DBSessionHelper.COLUMN_BYT_PNG_MAP
		};

		Cursor cursor = db.query(DBSessionHelper.TABLE_NAME,
				columns, DBSessionHelper.COLUMN_ID + " = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		byte[] buf = cursor.getBlob(0);
		cursor.close();
		logMe("getPngMapScreenShootById(id:" + id + ") = buf.len:" + (buf==null ? 0 : buf.length), dateStart);
		return buf;
	}

	public void setPngMapScreenShoot(Session session) {
		Date dateStart = new Date();
		long id = session.getId();

		logMe("Session updatePngMapScreenShoot with id: " + id);
		ContentValues values = new ContentValues();
		values.put(DBSessionHelper.COLUMN_BYT_PNG_MAP, session.getPngMapScreenShoot());

		db.update(DBSessionHelper.TABLE_NAME, values, DBSessionHelper.COLUMN_ID + " = " + id, null);
		logMe("updatePngMapScreenShoot(session.id:" + session.getId() + ")", dateStart);
	}

	public void delete(Session session) {
		Date dateStart = new Date();
		long id = session.getId();
		db.delete(DBSessionHelper.TABLE_NAME, DBSessionHelper.COLUMN_ID + " = " + id, null);
		logMe("delete(session.id:" + session.getId() + ")", dateStart);
	}

	public Session getById(long id) {
		Date dateStart = new Date();
		Cursor cursor = db.query(DBSessionHelper.TABLE_NAME,
				allColumns, DBSessionHelper.COLUMN_ID + " = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToLocalisation(cursor);
		cursor.close();
		logMe("getById(id:" + id + ")", dateStart);
		return newSession;
	}

	public List<Session> getAll() {
		Date dateStart = new Date();
		List<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(DBSessionHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Session session = cursorToLocalisation(cursor);
			sessions.add(session);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		logMe("getAll()", dateStart);
		return sessions;
	}

	public List<Session> getNotSend() {
		Date dateStart = new Date();
		List<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(DBSessionHelper.TABLE_NAME,
				allColumns, DBSessionHelper.COLUMN_TIME_SEND + " IS NULL", null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Session session = cursorToLocalisation(cursor);
			sessions.add(session);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		logMe("getNotSend()", dateStart);
		return sessions;
	}

	public Session getPrevious(Session session) {
		Date dateStart = new Date();
		long id = session.getId();
		logMe("Session getPrevious from id: " + id);
		Session ret = null;
		Cursor cursor = null;
		try {
			cursor = db.query(DBSessionHelper.TABLE_NAME,
					allColumns, DBSessionHelper.COLUMN_ID + " < " + id, null, null, null, DBSessionHelper.COLUMN_ID + " DESC", "1");
	
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				ret = cursorToLocalisation(cursor);
			}
		}
		finally {
			// Make sure to close the cursor
			if (cursor!=null)
				cursor.close();
		}
		logMe("getPrevious(session.id:" + session.getId() + ")", dateStart);
		return ret;
	}

	public Session getNext(Session session) {
		Date dateStart = new Date();
		long id = session.getId();
		logMe("Session getPrevious from id: " + id);
		Session ret = null;
		Cursor cursor = null;
		try {
			cursor = db.query(DBSessionHelper.TABLE_NAME,
					allColumns, DBSessionHelper.COLUMN_ID + " > " + id, null, null, null, DBSessionHelper.COLUMN_ID + " ASC", "1");
	
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				ret = cursorToLocalisation(cursor);
			}
		}
		finally {
			// Make sure to close the cursor
			if (cursor!=null)
				cursor.close();
		}
		logMe("getNext(session.id:" + session.getId() + ")", dateStart);
		return ret;
	}

	public void backupDbToSdcard() {
		Date dateStart = new Date();
		try {
			dbHelper.backupDbToSdcard();
	    } catch (Exception ex) {
	    	notificationMessage.notifyError(ex);
	    }
		logMe("backupDbToSdcard()", dateStart);
	}

	public void restoreDbFromSdcard() {
		Date dateStart = new Date();
		try {
			dbHelper.restoreDbFromSdcard();
	    } catch (Exception ex) {
	    	notificationMessage.notifyError(ex);
	    }
		logMe("restoreDbFromSdcard()", dateStart);
	}

	private Session cursorToLocalisation(Cursor cursor) {
		int col = 0;
		long time = 0;
		Session session = new Session();
		if (cursor.getColumnCount()>col)
			session.setId(cursor.getLong(col++));
		if (cursor.getColumnCount()>col)
			session.setIdSend(cursor.getLong(col++));
		if (cursor.getColumnCount()>col)
			session.setStart(new Localisation(cursor.getLong(col++)));
		if (cursor.getColumnCount()>col)
			session.setEnd(new Localisation(cursor.getLong(col++)));
		if (cursor.getColumnCount()>col) {
			time = cursor.getLong(col++);
			session.setTimeStart(time > 0 ? new Date(time) : null);
		}
		if (cursor.getColumnCount()>col) {
			time = cursor.getLong(col++);
			session.setTimeStop(time > 0 ? new Date(time) : null);
		}
		if (cursor.getColumnCount()>col) {
			time = cursor.getLong(col++);
			session.setTimeSend(time > 0 ? new Date(time) : null);
		}
		if (cursor.getColumnCount()>col)
			session.setCalculateDistance(cursor.getDouble(col++));
		if (cursor.getColumnCount()>col)
			session.setCalculateElapsedTime(cursor.getLong(col++));
		return session;
	}

	private void logMe(String msg, Date dateStart) {
		logMe("DB Execution time:" + (new Date().getTime() - dateStart.getTime()) + "millisecond - " + msg);
    }

	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}