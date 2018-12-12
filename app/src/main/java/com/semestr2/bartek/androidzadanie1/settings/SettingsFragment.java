package com.semestr2.bartek.androidzadanie1.settings;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.semestr2.bartek.androidzadanie1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        Switch s = v.findViewById(R.id.display_galery_in_list);
        boolean asGallery = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("display_galeries_in_list", false);
        s.setChecked(asGallery);
        s.setOnCheckedChangeListener((compoundButton, b) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("display_galeries_in_list", b).apply());
        return v;
    }

}
