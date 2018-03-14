package com.runningstars.db.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cameleon.common.db.sqlite.helper.DBAbstractHelper;
import com.cameleon.common.inotifier.INotifierMessage;

public class DBSessionHelper extends DBAbstractHelper {

	private static final String TAG = DBSessionHelper.class.getCanonicalName();

	public static final String TABLE_NAME = "SESSION";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_SEND = "ID_SEND";
	public static final String COLUMN_LOCATION_START = "LOCATION_START";
	public static final String COLUMN_LOCATION_END = "LOCATION_END";
	public static final String COLUMN_TIME_START = "TIME_START";
	public static final String COLUMN_TIME_SEND = "TIME_SEND";
	public static final String COLUMN_TIME_STOP = "TIME_STOP";
	public static final String COLUMN_CAL_DISTANCE = "CAL_DISTANCE";
	public static final String COLUMN_CAL_ELAPSED_TIME = "CAL_ELAPSED_TIME";
	public static final String COLUMN_BYT_PNG_MAP = "BYT_PNG_MAP";

	private static final String DATABASE_NAME = "GPSSession.db";
	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		COLUMN_ID_SEND + " INTEGER NULL, " + 
		COLUMN_LOCATION_START + " INTEGER NULL, " + 
		COLUMN_LOCATION_END + " INTEGER NULL, " + 
		COLUMN_TIME_START + " INTEGER NOT NULL," + 
		COLUMN_TIME_SEND + " INTEGER NULL," + 
		COLUMN_TIME_STOP + " INTEGER NULL, " + 
		COLUMN_CAL_DISTANCE + " REAL NULL, " + 
		COLUMN_CAL_ELAPSED_TIME + " INTEGER NULL, " + 
		COLUMN_BYT_PNG_MAP + " BLOB NULL" + 
	");";


	public DBSessionHelper(Context context, INotifierMessage notificationMessage) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, notificationMessage);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
        notificationMessage.notifyMessage("create database:" + database.getPath());

        database.execSQL(DATABASE_CREATE);
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}
}
