package com.semestr2.bartek.androidzadanie1.categories;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.favourites.FavouriteBooksFragment;
import com.semestr2.bartek.androidzadanie1.favourites.FavouriteCategoriesFragment;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Favourites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        ViewPager vp = v.findViewById(R.id.favourites_pager);
        ViewPagerAdapter vpa = new ViewPagerAdapter(getChildFragmentManager());
        vpa.addFragment(new FavouriteCategoriesFragment(), "Kategorie");
        vpa.addFragment(new FavouriteBooksFragment(), "Książki");
        vp.setAdapter(vpa);
        tabLayout.setupWithViewPager(vp);

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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
