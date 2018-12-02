package com.semestr2.bartek.androidzadanie1.categories;

import com.semestr2.bartek.androidzadanie1.books.Book;

public interface OnLikeListener {

    void setLike(Category category, boolean like);
    void setLike(Book category, boolean like);
}
