package com.semestr2.bartek.androidzadanie1.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.categories.Category;
import com.semestr2.bartek.androidzadanie1.categories.RecommendationAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    private OnHomeFragmentInteractionListener mListener;
    private AppCompatActivity mActivity;

    private RecommendationAdapter rc;

    public HomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager vp = v.findViewById(R.id.promotions_view_pager);
        DatabaseAccess instance = DatabaseAccess.getInstance(getContext());
        vp.setAdapter(new PromotionsPagerAdapter(this, instance, vp));
        GridView recommendationsView = v.findViewById(R.id.recommendations);
        recommendationsView.setNumColumns(2);
        ArrayList<Category> recommends = instance.getRecommendations(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_userName", null));
        rc = new RecommendationAdapter(mActivity, recommends);
        recommendationsView.setAdapter(rc);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.setHomeIcon();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mActivity = (AppCompatActivity) context;
            mListener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void displayBookDetails(Book b){
        Uri uri = new Uri.Builder()
                .appendPath("home")
                .appendPath("displayBook")
                .build();
        mListener.onHomeFragmentInteraction(uri, b);
    }

    public void refreshData() {
        DatabaseAccess instance = DatabaseAccess.getInstance(getContext());
        ArrayList<Category> recommends = instance.getRecommendations(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_userName", null));
        rc.clear();
        rc.addAll(recommends);
        rc.notifyDataSetChanged();
    }

    public interface OnHomeFragmentInteractionListener {
        //void onHomeFragmentInteraction(Uri uri);
        void onHomeFragmentInteraction(Uri uri, Object data);
        void setHomeIcon();
    }


}
