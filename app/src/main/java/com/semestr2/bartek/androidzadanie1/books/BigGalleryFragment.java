package com.semestr2.bartek.androidzadanie1.books;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;

public class BigGalleryFragment extends BookFragment {

    private OnFragmentInteractionListener mListener;
    private Book mBook;
    ViewPager gallery;
    public BigGalleryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_big_gallery, container, false);
        gallery = v.findViewById(R.id.big);
        gallery.setAdapter(new BookGalleryAdapter(this, mBook, gallery));
        ListView lv = v.findViewById(R.id.book_list_item_root);
        RecyclerView smallGallery = v.findViewById(R.id.small);
        smallGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        smallGallery.setAdapter(new SimpleCoversAdapter(mBook));
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

    @Override
    public void getDetails(Book book) {

    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book mBook) {
        this.mBook = mBook;
    }

    private class SimpleCoversAdapter extends RecyclerView.Adapter<SimpleCoversAdapter.ViewHolder> {
        private Book mBook;
        private ArrayList<Bitmap> mValues = new ArrayList<>();
        int originalSize;
        public SimpleCoversAdapter(Book mBook) {
            this.mBook = mBook;
            if(mBook.getCover()!=null){
                mValues.add(BitmapFactory.decodeByteArray(mBook.getCover(), 0, mBook.getCover().length));
            }
            if(mBook.getAltCover()!=null){
                mValues.add(BitmapFactory.decodeByteArray(mBook.getAltCover(), 0, mBook.getAltCover().length));
            }
            originalSize = mValues.size();
            mValues.addAll(mValues);
            mValues.addAll(mValues);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.simple_image_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SimpleCoversAdapter.ViewHolder holder, int position) {
            holder.mImage.setImageBitmap(mValues.get(position));
            holder.mView.setOnClickListener(v -> gallery.setCurrentItem(position%originalSize));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final ImageView mImage;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mImage = view.findViewById(R.id.imageView);
            }

        }

    }
}
