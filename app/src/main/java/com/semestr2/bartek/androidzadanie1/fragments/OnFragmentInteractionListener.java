package com.semestr2.bartek.androidzadanie1.fragments;

import android.net.Uri;

import com.semestr2.bartek.androidzadanie1.books.Book;

public interface OnFragmentInteractionListener {
    void onBookDetailsInteraction(Uri uri);
    void onAddToCartListener(Book book, boolean isBuyNow);
    void setHomeAsUp();
}
