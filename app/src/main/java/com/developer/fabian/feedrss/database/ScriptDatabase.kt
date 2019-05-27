package com.developer.fabian.feedrss.database

import android.provider.BaseColumns

object ScriptDatabase {

    internal const val ENTER_TABLE_NAME = "enterT"
    private const val STRING_TYPE = "TEXT"
    private const val INT_TYPE = "INTEGER"

    internal const val CREATE_ENTER = "CREATE TABLE $ENTER_TABLE_NAME(${EnterColumns.ID} " +
            "$INT_TYPE PRIMARY KEY AUTOINCREMENT, ${EnterColumns.TITLE} $STRING_TYPE NOT NULL, " +
            "${EnterColumns.DESCRIPTION} $STRING_TYPE, ${EnterColumns.URL} $STRING_TYPE, " +
            "${EnterColumns.THUMB_URL} $STRING_TYPE)"

    object EnterColumns {
        internal const val ID = BaseColumns._ID
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val URL = "url"
        const val THUMB_URL = "thumb_url"
    }
}
