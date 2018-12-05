package com.semestr2.bartek.androidzadanie1.books;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.basket.BookOrderDialogBuilder;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

public class BookDetailsFragment extends BookFragment {
    private Book mBook;

    private OnFragmentInteractionListener mListener;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_details, container, false);
        ViewPager galery = v.findViewById(R.id.book_galery);
        galery.setAdapter(new BookGalleryAdapter(this, mBook, galery));
        Button button = v.findViewById(R.id.add_to_basket);
        button.setOnClickListener((e)-> BookOrderDialogBuilder.showOrderDialog(getContext(), mBook, mListener, container));
        TextView price = v.findViewById(R.id.price);
        price.setText("Cena: " + mBook.getPrice());
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setBook(Book b) {
        mBook = b;
    }


    @Override
    public void getDetails(Book book) {
        mListener.displayBigGallery(book);
    }
}
