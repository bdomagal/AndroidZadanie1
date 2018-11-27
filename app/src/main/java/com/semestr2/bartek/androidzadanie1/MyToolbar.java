package com.semestr2.bartek.androidzadanie1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyToolbar extends Toolbar {

    private Menu menu;
    private AppCompatActivity activity;
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

    public Menu inflateMenu(final AppCompatActivity activity, Menu menu) {
        this.menu = menu;
        this.activity = activity;
        activity.getMenuInflater().inflate(R.menu.main_menu, menu);

        menu.findItem(R.id.basket_view).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Intent intent = new Intent(activity, BasketActivity.class);
                        activity.startActivity(intent);
                        return true;
                    }
                });

        final MenuItem login = menu.findItem(R.id.login);
        login.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivityForResult(intent, LOGIN);
                        return true;
                    }
                });
        final MenuItem logout = menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(activity, "Bye, " + PreferenceManager.getDefaultSharedPreferences(activity).getString("pref_userName", null),
                                Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(activity).edit();
                        edit.putString("pref_userName", null);
                        edit.commit();
                        login.setVisible(true);
                        logout.setVisible(false);
                        return true;
                    }
                });
        final String user = PreferenceManager.getDefaultSharedPreferences(activity).getString("pref_userName", null);
        if (user == null) {
            logout.setVisible(false);
        } else {
            login.setVisible(false);
        }
        menu.findItem(R.id.categories).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Intent intent = new Intent(activity, CategoriesActivity.class);
                        activity.startActivity(intent);
                        return true;
                    }
                });
        menu.findItem(R.id.favourites).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Intent intent = new Intent(activity, FavouritesActivity.class);
                        activity.startActivity(intent);
                        return true;
                    }
                });
        menu.findItem(R.id.settings).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final Intent intent = new Intent(activity, SettingsActivity.class);
                        activity.startActivity(intent);
                        return true;
                    }
                });
        return menu;
    }

    public void loginSuccess(){
        MenuItem login = menu.findItem(R.id.login);
        MenuItem logout = menu.findItem(R.id.logout);
        login.setVisible(false);
        logout.setVisible(true);
    }
}
