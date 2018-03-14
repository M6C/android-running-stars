package com.runningstars.db.sqlite.datasource;

import android.database.Cursor;

class DbTool {

    private static DbTool instance = null;

    public static DbTool getInstance() {
        if (instance == null) {
            instance = new DbTool();
        }
        return instance;
    }

    public long toLong(Cursor cursor, int i) {
        return cursor.getLong(i);
    }
}
