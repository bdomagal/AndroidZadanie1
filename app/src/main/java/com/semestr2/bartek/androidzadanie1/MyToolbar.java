package com.semestr2.bartek.androidzadanie1;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

public class MyToolbar extends Toolbar {

    private Menu menu;
    public static final int LOGIN = 1;

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Menu inflateMenu(final MainActivity activity, Menu menu) {
        this.menu = menu;
        activity.getMenuInflater().inflate(R.menu.main_menu, menu);

        menu.findItem(R.id.basket_view).setOnMenuItemClickListener(item -> {
            activity.swapFragmentTo("BASKET", false);
            return true;
        });

        final MenuItem login = menu.findItem(R.id.login);
        login.setOnMenuItemClickListener(item -> {
            activity.startActivityForResult(new Intent(activity, LoginActivity.class), LOGIN);
            return true;
        });
        final MenuItem logout = menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(item -> activity.logoutAndRefresh(menu, logout));
        menu.findItem(R.id.categories).setOnMenuItemClickListener(item -> {
            activity.swapFragmentTo("CATEGORIES", false);
            return true;
        });
        menu.findItem(R.id.favourites).setOnMenuItemClickListener(item -> {
            activity.swapFragmentTo("FAVOURITES", false);
            return true;
        });
        menu.findItem(R.id.settings).setOnMenuItemClickListener(item -> {
            activity.swapFragmentTo("SETTINGS", false);
            return true;
        });

        if (PreferenceManager.getDefaultSharedPreferences(activity).getString("pref_userName", null) == null) {
            logout.setVisible(false);
        } else {
            login.setVisible(false);
        }

        return menu;
    }

    public void loginSuccess() {
        MenuItem login = menu.findItem(R.id.login);
        MenuItem logout = menu.findItem(R.id.logout);
        login.setVisible(false);
        logout.setVisible(true);
    }
}
