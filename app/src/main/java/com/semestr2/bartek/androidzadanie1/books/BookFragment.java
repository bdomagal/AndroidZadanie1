package com.semestr2.bartek.androidzadanie1.books;


import android.support.v4.app.Fragment;

abstract class BookFragment extends Fragment {
    public abstract void getDetails(Book book);
}
