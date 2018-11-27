package com.semestr2.bartek.androidzadanie1.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;

import java.util.ArrayList;

class PromotionsPagerAdapter extends PagerAdapter{

    ArrayList<Book> data;
    AppCompatActivity mContext;

    public PromotionsPagerAdapter(AppCompatActivity context, DatabaseAccess instance) {
        mContext = context;
        instance.open();
        data = instance.getPromotions();
        instance.close();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup layout = (ViewGroup)mContext.getLayoutInflater().inflate(R.layout.galery_item, container, false);
        ((TextView)layout.findViewById(R.id.galery_item_title)).setText(data.get(position).getTitle());
        byte[] cover = data.get(position).getCover();
        ((ImageView)layout.findViewById(R.id.galery_item_image)).setImageBitmap(BitmapFactory.decodeByteArray(cover, 0, cover.length));
        container.addView(layout);
        ((ImageView)layout.findViewById(R.id.galery_left)).setOnClickListener((v) ->((ViewPager)mContext.findViewById(R.id.promotions_view_pager)).setCurrentItem((position-1)>=0? position-1 : data.size()-1,true));
        ((ImageView)layout.findViewById(R.id.galery_right)).setOnClickListener((v) ->((ViewPager)mContext.findViewById(R.id.promotions_view_pager)).setCurrentItem((position+1)%data.size(),true));
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


}
