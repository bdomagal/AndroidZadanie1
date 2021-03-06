package com.semestr2.bartek.androidzadanie1.categories;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;
import com.semestr2.bartek.androidzadanie1.home.HomeFragment;

import java.util.ArrayList;

public class RecommendationAdapter extends ArrayAdapter<Category> {

    private final Activity context;
    private final ArrayList<Category> objects;
    private final HomeFragment.OnHomeFragmentInteractionListener mListener;


    public RecommendationAdapter(@NonNull Activity context, @NonNull ArrayList<Category> objects, HomeFragment.OnHomeFragmentInteractionListener l) {
        super(context, R.layout.recommendation, objects);
mListener = l;
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = convertView;
        if(convertView == null) {
            rowView = inflater.inflate(R.layout.recommendation, parent, false);
        }
        TextView title = rowView.findViewById(R.id.title);
        ImageView imageView = rowView.findViewById(R.id.image);
        CheckBox checkBox = rowView.findViewById(R.id.favourite);
        checkBox.setOnCheckedChangeListener(null);

        //this code sets the values of the objects to values from the arrays
        Category item = objects.get(position);
        title.setText(item.getName());

        checkBox.setChecked(item.isChecked());
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            String user = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_userName", null);
            if(user!=null) {
                DatabaseAccess instance = DatabaseAccess.getInstance(context);
                instance.setLike(item, b, user);
            }
        });
        if(item.getImage()!=null){
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        }

        rowView.setOnClickListener(a->mListener.onCategoryClick(item));

        return rowView;
    }

//    public static class OnItemClickListener implements AdapterView.OnItemClickListener {
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//            CheckBox cb = view.findViewById(R.id.genre_select);
//            Category c = (Category) adapterView.getItemAtPosition(i);
//            c.toggle();
//            cb.toggle();
//        }
//    }

}
