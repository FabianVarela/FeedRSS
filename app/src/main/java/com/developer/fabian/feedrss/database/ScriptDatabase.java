package com.developer.fabian.feedrss.database;

import android.provider.BaseColumns;

public class ScriptDatabase {

    static final String ENTER_TABLE_NAME = "enterT";
    private static final String STRING_TYPE = "TEXT";
    private static final String INT_TYPE = "INTEGER";

    static final String CREATE_ENTER = "CREATE TABLE " + ENTER_TABLE_NAME + "(" +
            EnterColumns.ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
            EnterColumns.TITLE + " " + STRING_TYPE + " NOT NULL," +
            EnterColumns.DESCRIPTION + " " + STRING_TYPE + "," +
            EnterColumns.URL + " " + STRING_TYPE + "," +
            EnterColumns.THUMB_URL + " " + STRING_TYPE + ")";

    public static class EnterColumns {
        static final String ID = BaseColumns._ID;
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URL = "url";
        public static final String THUMB_URL = "thumb_url";
    }
}
