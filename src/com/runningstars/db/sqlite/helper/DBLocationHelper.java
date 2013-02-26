package com.runningstars.db.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.runningstars.activity.inotifier.INotifierMessage;

public class DBLocationHelper extends DBAbstractHelper {

	private static final String TAG = DBLocationHelper.class.getCanonicalName();

	public static final String TABLE_NAME = "LOCATION";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_SESSION = "ID_SESSION";
	public static final String COLUMN_SPEED = "SPEED";
	public static final String COLUMN_LONGITUDE = "LONGITUDE";
	public static final String COLUMN_LATITUDE = "LATITUDE";
	public static final String COLUMN_ALTITUDE = "ALTITUDE";
	public static final String COLUMN_ACCURACY = "ACCURACY";
	public static final String COLUMN_BEARING = "BEARING";
	public static final String COLUMN_TIME = "TIME";
	public static final String COLUMN_ADDRESSLINE = "ADDRESSLINE";
	public static final String COLUMN_POSTALCODE = "POSTALCODE";
	public static final String COLUMN_LOCALITY = "LOCALITY";
	public static final String COLUMN_TIME_SEND = "TIME_SEND";
	public static final String COLUMN_CAL_DISTANCE = "CAL_DISTANCE";
	public static final String COLUMN_CAL_DISTANCE_CUMUL = "CAL_DISTANCE_CUMUL";
	public static final String COLUMN_CAL_ELAPSED_TIME = "CAL_ELAPSED_TIME";
	public static final String COLUMN_CAL_ELAPSED_TIME_CUMUL = "CAL_ELAPSED_TIME_CUMUL";

	private static final String DATABASE_NAME = "GPSlocation.db";
	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		COLUMN_ID_SESSION + " INTEGER NOT NULL, " + 
		COLUMN_SPEED + " REAL NULL, " + 
		COLUMN_LONGITUDE + " REAL NULL, " + 
		COLUMN_LATITUDE + " REAL NULL, " + 
		COLUMN_ALTITUDE + " REAL NULL, " + 
		COLUMN_ACCURACY + " REAL NULL, " + 
		COLUMN_BEARING + " REAL NULL, " + 
		COLUMN_TIME + " INTEGER NULL, " + 
		COLUMN_ADDRESSLINE + " TEXT NULL, " + 
		COLUMN_POSTALCODE + " TEXT NULL, " + 
		COLUMN_LOCALITY + " TEXT NULL, " + 
		COLUMN_TIME_SEND + " INTEGER NULL, " + 
		COLUMN_CAL_DISTANCE + " REAL NULL, " + 
		COLUMN_CAL_DISTANCE_CUMUL + " REAL NULL, " +
		COLUMN_CAL_ELAPSED_TIME + " INTEGER NULL, " + 
		COLUMN_CAL_ELAPSED_TIME_CUMUL + " INTEGER NULL " + 
	");";

	public DBLocationHelper(Context context, INotifierMessage notificationMessage) {
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
