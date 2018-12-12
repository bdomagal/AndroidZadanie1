package com.semestr2.bartek.androidzadanie1.fragments;

import android.net.Uri;

import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.categories.Category;

public interface OnFragmentInteractionListener {
    void onBookDetailsInteraction(Uri uri);
    void onAddToCartListener(Book book, boolean isBuyNow);
    void setHomeAsUp();
    void onListFragmentInteraction(Book book);
    void onDisplayDetailsListener(Book book);

    void displayBigGallery(Book book);

    void onCategoryClick(Category item);

    void displayBasket();

    // void markFavourite();
}
