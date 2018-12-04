package com.semestr2.bartek.androidzadanie1.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Bookstore.db";
    private static final int DATABASE_VERSION = 1;

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
