package com.semestr2.bartek.androidzadanie1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

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

    public boolean authenticate(String mEmail, String mPassword) {
        Cursor cursor = database.rawQuery("SELECT * FROM Users WHERE login = ? AND password = ? ", new String[] {mEmail, mPassword});
        if(cursor.getCount()==1){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Category> getRecommendations(String user) {
        ArrayList<Category> recs = new ArrayList<>();
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM Categories AS c JOIN Recommendations AS r ON c.name = r.category LEFT JOIN (SELECT * FROM CategoryLikes WHERE user = ?) AS l ON c.name = l.category", new String[]{user});
        }else{
            cursor = database.rawQuery("SELECT * FROM Categories AS c JOIN Recommendations AS r ON c.name = r.category LEFT JOIN (SELECT * FROM CategoryLikes WHERE user = null) AS l ON c.name = l.category", null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category item = new Category(cursor.getString(0), cursor.getString(1), cursor.getBlob(2));
            item.setChecked(cursor.getString(5)!=null);
            recs.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return recs;
    }

    public void setLike(Category item, boolean b, String user) {

        ContentValues cv = new ContentValues();

        if(b) {
            cv.put("user", user);
            cv.put("category", item.getName());
            database.insert("CategoryLikes", null, cv);
            //database.rawQuery("INSERT INTO CategoryLikes (user, category) VALUES (?, ?)", new String[]{user, item.getName()});
        }
        else{
            database.delete("CategoryLikes", "user = ? AND category = ?", new String[] {user, item.getName()});
            //database.rawQuery("DELETE FROM CategoryLikes WHERE user = ? AND category = ?", new String[]{user, item.getName()});
        }

    }

    public ArrayList<Category> getCategoriesWithLikes(String user) {
        ArrayList<Category> recs = new ArrayList<>();
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM Categories AS c LEFT JOIN (SELECT * FROM CategoryLikes WHERE user = ?) AS l ON c.name = l.category", new String[]{user});
        }else{
            cursor = database.rawQuery("SELECT * FROM Categories AS c LEFT JOIN (SELECT * FROM CategoryLikes WHERE user = null) AS l ON c.name = l.category", null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
            Category item = new Category(cursor.getString(0), cursor.getString(1), cursor.getBlob(2));
            item.setChecked(cursor.getString(4)!=null);
            recs.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return recs;

    }
}
