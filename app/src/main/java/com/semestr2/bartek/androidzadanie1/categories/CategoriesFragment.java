package com.semestr2.bartek.androidzadanie1.categories;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {

    private CategoriesLikesAdapter adapter;

    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
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
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        ListView lv = v.findViewById(R.id.categories_list_view);

        adapter = new CategoriesLikesAdapter(mActivity, DatabaseAccess.getInstance(getContext())
                .getCategoriesWithLikes(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("pref_userName", null)
        ));
        lv.setAdapter(adapter);

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mActivity = (AppCompatActivity) context;
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

    private class CategoriesLikesAdapter extends ArrayAdapter<Category> {

        private final Activity context;
        private final ArrayList<Category> objects;

        CategoriesLikesAdapter(@NonNull Activity context, @NonNull ArrayList<Category> objects) {
            super(context, R.layout.categories_drawer_item, objects);

            this.objects = objects;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String user = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_userName", null);
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.categories_drawer_item, parent, false);
            }

            //this code gets references to objects in the listview_row.xml file
            TextView nameTextField = rowView.findViewById(R.id.genre);
            TextView groupTextField = rowView.findViewById(R.id.group);
            ImageView imageView = rowView.findViewById(R.id.genre_icon);
            CheckBox checkBox = rowView.findViewById(R.id.genre_select);
            checkBox.setButtonDrawable(android.R.drawable.btn_star);

            //this code sets the values of the objects to values from the arrays
            Category item = objects.get(position);
            nameTextField.setText(item.getName());
            groupTextField.setText(item.getCategoryGroup());
            if(user==null){
                checkBox.setClickable(false);
            }else{
                checkBox.setClickable(true);
            }
            checkBox.setChecked(item.isChecked());
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {

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

            return rowView;
        }
    }

    public void refreshData() {
        DatabaseAccess instance = DatabaseAccess.getInstance(getContext());
        ArrayList<Category> recommends = instance.getCategoriesWithLikes(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_userName", null));
        adapter.clear();
        adapter.addAll(recommends);
        adapter.notifyDataSetChanged();
    }

}
