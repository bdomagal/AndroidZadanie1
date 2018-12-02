package com.semestr2.bartek.androidzadanie1.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semestr2.bartek.androidzadanie1.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;

import java.util.ArrayList;

class BookGaleryAdapter extends PagerAdapter{

    ArrayList<Bitmap> data;
    BookDetailsFragment mContext;
    final ViewPager parent;

    public BookGaleryAdapter(BookDetailsFragment context, Book b, ViewPager parent) {
        mContext = context;
        this.parent = parent;
        data = new ArrayList<>();
        if(b==null){
            return;
        }
        if(b.getCover()!=null) {
            data.add(BitmapFactory.decodeByteArray(b.getCover(), 0, b.getCover().length));
        }
        if(b.getCover()!=null) {
            data.add(BitmapFactory.decodeByteArray(b.getAltCover(), 0, b.getAltCover().length));
        }
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
        ImageView image = layout.findViewById(R.id.galery_item_image);
        image.setImageBitmap(data.get(position));
        image.setOnLongClickListener((v)-> {
            //TODO - Add swap to big galery view
            return false;
        });
        container.addView(layout);
        ((ImageView)layout.findViewById(R.id.galery_left)).setOnClickListener((v) ->parent.setCurrentItem((position-1)>=0? position-1 : data.size()-1,true));
        ((ImageView)layout.findViewById(R.id.galery_right)).setOnClickListener((v) ->parent.setCurrentItem((position+1)%data.size(),true));
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


}
