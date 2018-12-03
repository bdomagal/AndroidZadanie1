package com.semestr2.bartek.androidzadanie1.fragments;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;

import java.util.ArrayList;

class CarouselAdapter extends RecyclerView.Adapter {
    ArrayList<Book> books;
    public CarouselAdapter(DatabaseAccess instance) {
        books = instance.getPromotions();
        Log.e("AAAAAAAAAAAAAAAAAAA", books.size()+"");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.galery_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Book b = books.get(position);
        Log.e("AAAAAAAAAAAAAAAAAAA", b.getTitle());
        byte[] cover = b.getCover();
        ((ViewHolder)holder).tv.setText(b.getTitle());
        ((ViewHolder)holder).iv.setImageBitmap(BitmapFactory.decodeByteArray(cover, 0, cover.length));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.galery_item_title);
            iv = itemView.findViewById(R.id.galery_item_image);
        }
    }
}
