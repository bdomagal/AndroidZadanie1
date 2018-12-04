package com.semestr2.bartek.androidzadanie1.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semestr2.bartek.androidzadanie1.R;

import java.util.ArrayList;

class BookGalleryAdapter extends PagerAdapter{

    private ArrayList<Bitmap> data;
    private BookDetailsFragment mContext;
    private final ViewPager parent;

    BookGalleryAdapter(BookDetailsFragment context, Book b, ViewPager parent) {
        mContext = context;
        this.parent = parent;
        data = new ArrayList<>();
        if(b==null){
            return;
        }
        if(b.getCover()!=null) {
            data.add(BitmapFactory.decodeByteArray(b.getCover(), 0, b.getCover().length));
        }
        if(b.getAltCover()!=null) {
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
        ImageView image = layout.findViewById(R.id.galery_item_image);
        image.setImageBitmap(data.get(position));
        image.setOnLongClickListener((v)-> false);
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
