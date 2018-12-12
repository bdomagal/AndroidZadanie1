package com.semestr2.bartek.androidzadanie1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.categories.Category;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DatabaseAccess
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
        ArrayList<Book> list = new ArrayList<>();
        while(list.size()<4) {
            Cursor cursor = database.rawQuery("SELECT * FROM Book", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    public boolean authenticate(String mEmail, String mPassword) {
        Cursor cursor = database.rawQuery("SELECT * FROM Users WHERE login = ? AND password = ? ", new String[] {mEmail, mPassword});
        boolean isAuthenticated = cursor.getCount() == 1;
        cursor.close();
        return true;
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
    public void setLike(Book item, boolean b, String user) {

        ContentValues cv = new ContentValues();

        System.out.println(item.getTitle());
        if(b) {
            cv.put("user", user);
            cv.put("books", item.getId());
            cv.put("stars", 5);
            database.insert("BookLikes", null, cv);
            //database.rawQuery("INSERT INTO CategoryLikes (user, category) VALUES (?, ?)", new String[]{user, item.getName()});
        }
        else{
            database.delete("BookLikes", "user = ? AND books = ?", new String[] {user, item.getId()+""});
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

    public ArrayList<Book> findFilteredBooks(Collection<Category> data, String user) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Book b LEFT JOIN (SELECT * FROM BookLikes WHERE user = ?) AS bl ON b.id = bl.books LEFT JOIN Basket ba ON b.id = ba.item WHERE ");
        ArrayList<Book> list = new ArrayList<>();
        boolean any = true;
        for (Category datum : data) {
            if(datum.isChecked()){
                queryBuilder.append(String.format("Genre = '%s' OR ", datum.getName()));
                any = false;
            }
        }
        String query = any ? "SELECT * FROM Book b LEFT JOIN (SELECT * FROM BookLikes WHERE user = ?) AS bl ON b.id = bl.books LEFT JOIN Basket ba ON b.id = ba.item" : queryBuilder.toString().substring(0, queryBuilder.length()-3);
        Cursor cursor = database.rawQuery(query, new String[]{user});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book b = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0));
            list.add(b);
            b.setLiked(cursor.getString(7)!=null);
            if(cursor.getString(11)!=null)
            b.setAmount(cursor.getInt(11));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Book getBasketItem(Book mBook) {
        String query = "SELECT count FROM Basket WHERE item = " + mBook.getId();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        if(cursor.getCount()==1){
            int amount = cursor.getInt(0);
            cursor.close();
            return new Book(mBook, amount);
        }
        else{
            cursor.close();
            return new Book(mBook, 0);
        }
    }

    public void addToBasket(Book b) {
        if(b.getAmount()==0){
            database.delete("Basket", "item = ?", new String[]{b.getId()+""});
        }
        else{
            ContentValues cv = new ContentValues();
            cv.put("item", b.getId());
            cv.put("count", b.getAmount());
            int id = (int) database.insertWithOnConflict("Basket", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            if (id == -1) {
                database.update("Basket", cv, "item=?", new String[] {b.getId()+""});
            }
        }
    }

    public ArrayList<Book> getBasketItems() {
        ArrayList<Book> list = new ArrayList<>();
        String query = "SELECT * FROM Book b JOIN Basket ba ON b.id = ba.item";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
            Book b = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0));
            b.setAmount(cursor.getInt(8));
            list.add(b);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<Category> getFavouriteCategories(String user) {
        ArrayList<Category> recs = new ArrayList<>();
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM Categories AS c JOIN (SELECT * FROM CategoryLikes WHERE user = ?) AS l ON c.name = l.category", new String[]{user});
        }else{
            cursor = database.rawQuery("SELECT * FROM Categories AS c JOIN (SELECT * FROM CategoryLikes WHERE user = null) AS l ON c.name = l.category", null);
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

    public ArrayList<Book> getBooks(String user) {
        ArrayList<Book> list = new ArrayList<>();
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM Book AS c LEFT JOIN (SELECT * FROM BookLikes WHERE user = ?) AS l ON c.id = l.books", new String[]{user});
        }else{
            cursor = database.rawQuery("SELECT * FROM Book AS c LEFT JOIN (SELECT * FROM BookLikes WHERE user = null) AS l ON c.id = l.books", null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
            Book b = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0));
            b.setLiked(cursor.getString(7)!=null);
            list.add(b);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }
    public ArrayList<Book> getFavouriteBooks(String user) {
        ArrayList<Book> list = new ArrayList<>();
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM Book AS c JOIN (SELECT * FROM BookLikes WHERE user = ?) AS l ON c.id = l.books", new String[]{user});
        }else{
            cursor = database.rawQuery("SELECT * FROM Book AS c JOIN (SELECT * FROM BookLikes WHERE user = null) AS l ON c.id = l.books", null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
            Book b = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0));
            b.setLiked(cursor.getString(7)!=null);
            list.add(b);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public Book getBook(String user, int id) {
        Book book = null;
        Cursor cursor;
        if(user!=null) {
            cursor = database.rawQuery("SELECT * FROM (SELECT * FROM Book WHERE id = ?) AS c LEFT JOIN (SELECT * FROM BookLikes WHERE user = ?) AS l ON c.id = l.books LEFT JOIN Basket ba ON c.id = ba.item", new String[]{id+"", user});
        }else{
            cursor = database.rawQuery("SELECT * FROM (SELECT * FROM Book WHERE id = ?) AS c LEFT JOIN (SELECT * FROM BookLikes WHERE user = null) AS l ON c.id = l.books LEFT JOIN Basket ba ON c.id = ba.item", new String[]{id+""});
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && !cursor.isBeforeFirst()) {
            book = new Book(cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getBlob(4), cursor.getBlob(6), cursor.getDouble(3), cursor.getInt(0));
            book.setLiked(cursor.getString(7)!=null);
            if(cursor.getString(11)!=null) {
                book.setAmount(cursor.getInt(11));
            }
            else{
                book.setAmount(0);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return book;
    }
}
