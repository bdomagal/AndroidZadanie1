package com.semestr2.bartek.androidzadanie1.categories;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CategoriesLikesAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        ListView lv = v.findViewById(R.id.categories_list_view);
        ;
        adapter = new CategoriesLikesAdapter(getActivity(), DatabaseAccess.getInstance(getContext())
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

        public CategoriesLikesAdapter(@NonNull Activity context, @NonNull ArrayList<Category> objects) {
            super(context, R.layout.categories_drawer_item, objects);

            this.objects = objects;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String user = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_userName", null);
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.categories_drawer_item, null,true);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
