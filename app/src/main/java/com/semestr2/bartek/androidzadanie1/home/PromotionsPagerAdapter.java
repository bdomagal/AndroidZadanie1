package com.semestr2.bartek.androidzadanie1.home;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;

import java.util.ArrayList;

class PromotionsPagerAdapter extends PagerAdapter{

    private ArrayList<Book> data;
    private HomeFragment mContext;
    private final ViewPager parent;

    PromotionsPagerAdapter(HomeFragment context, DatabaseAccess instance, ViewPager parent) {
        mContext = context;
        data = instance.getPromotions();
        this.parent = parent;
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
        //((TextView)layout.findViewById(R.id.galery_item_title)).setText(data.get(position).getTitle());
        byte[] cover = data.get(position).getCover();
        ImageView image = layout.findViewById(R.id.galery_item_image);
        image.setImageBitmap(BitmapFactory.decodeByteArray(cover, 0, cover.length));
        image.setOnClickListener((v)-> mContext.displayBookDetails(data.get(position)));
        container.addView(layout);
        (layout.findViewById(R.id.galery_left)).setOnClickListener((v) ->parent.setCurrentItem((position-1)>=0? position-1 : data.size()-1,true));
        (layout.findViewById(R.id.galery_right)).setOnClickListener((v) ->parent.setCurrentItem((position+1)%data.size(),true));
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }


}
