package com.semestr2.bartek.androidzadanie1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public ArrayList<Category> getCategories() {
        ArrayList<Category> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Category(cursor.getString(0), cursor.getString(1), cursor.getBlob(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Book> getPromotions() {
        //TODO - Method Substitute
        ArrayList<Book> list = new ArrayList<>();
        while(list.size()<4) {
            Cursor cursor = database.rawQuery("SELECT * FROM Book", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }
}
