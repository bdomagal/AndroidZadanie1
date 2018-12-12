package com.semestr2.bartek.androidzadanie1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.semestr2.bartek.androidzadanie1.basket.Basket;
import com.semestr2.bartek.androidzadanie1.basket.BookOrderDialogBuilder;
import com.semestr2.bartek.androidzadanie1.books.BigGalleryFragment;
import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.books.BookDetailsFragment;
import com.semestr2.bartek.androidzadanie1.books.BookListFragment;
import com.semestr2.bartek.androidzadanie1.categories.CategoriesArrayAdapter;
import com.semestr2.bartek.androidzadanie1.categories.CategoriesFragment;
import com.semestr2.bartek.androidzadanie1.categories.Category;
import com.semestr2.bartek.androidzadanie1.categories.Favourites;
import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;
import com.semestr2.bartek.androidzadanie1.home.HomeFragment;
import com.semestr2.bartek.androidzadanie1.settings.SettingsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteractionListener, OnFragmentInteractionListener {
    final private MainActivity self = this;
    private DrawerLayout categoriesDrawer;
    private MyToolbar myToolbar;
    private DatabaseAccess databaseAccess;
    private Map<String, Fragment> fragments = new HashMap<>();
    private CategoriesArrayAdapter filtersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        setupToolbar();

        loadCategoriesList(databaseAccess);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fm = manager.beginTransaction();
        fragments.put("HOME", new HomeFragment());
        fm.replace(R.id.page_content, fragments.get("HOME"), "HOME");
        fragments.put("DETAILS", new BookDetailsFragment());
        fm.add(R.id.page_content, fragments.get("DETAILS"), "DETAILS");
        fragments.put("CATEGORIES", new CategoriesFragment());
        fm.add(R.id.page_content, fragments.get("CATEGORIES"), "CATEGORIES");
        fragments.put("BOOK_LIST", new BookListFragment());
        fm.add(R.id.page_content, fragments.get("BOOK_LIST"), "BOOK_LIST");
        fm.detach(fragments.get("DETAILS"));
        fm.detach(fragments.get("CATEGORIES"));
        fm.detach(fragments.get("BOOK_LIST"));
        fragments.put("GALLERY_FRAG", new BigGalleryFragment());
        fm.add(R.id.page_content, fragments.get("GALLERY_FRAG"), "GALLERY_FRAG");
        fm.detach(fragments.get("GALLERY_FRAG"));
        fragments.put("BASKET", new Basket());
        fm.add(R.id.page_content, fragments.get("BASKET"), "BASKET");
        fm.detach(fragments.get("BASKET"));
        fragments.put("FAVOURITES", new Favourites());
        fm.add(R.id.page_content, fragments.get("FAVOURITES"), "FAVOURITES");
        fm.detach(fragments.get("FAVOURITES"));
        fragments.put("SETTINGS", new SettingsFragment());
        fm.add(R.id.page_content, fragments.get("SETTINGS"), "SETTINGS");
        fm.detach(fragments.get("SETTINGS"));
        fm.commit();

    }

    @Override
    protected void onDestroy() {
        databaseAccess.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadCategoriesList(DatabaseAccess databaseAccess) {
        ListView categoriesList = findViewById(R.id.categories_drawer);
        filtersAdapter = new CategoriesArrayAdapter(this, databaseAccess.getCategories());
        //categoriesList.setOnItemClickListener(new CategoriesArrayAdapter.OnItemClickListener());
        categoriesList.setAdapter(filtersAdapter);
    }

    private void setupToolbar() {
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.burger_menu_icon);

        categoriesDrawer = findViewById(R.id.drawer_layout);
        Button filter = categoriesDrawer.findViewById(R.id.filter);
        filter.setOnClickListener(v -> {
            BookListFragment blf = (BookListFragment) fragments.get("BOOK_LIST");
            blf.setData(databaseAccess.findFilteredBooks(filtersAdapter.getData(), PreferenceManager.getDefaultSharedPreferences(self).getString("pref_userName", null)));
            swapFragmentTo("BOOK_LIST", false); categoriesDrawer.closeDrawer(GravityCompat.START);});
        myToolbar.setNavigationOnClickListener(v -> activateDrawer());
        if(getSupportActionBar()!=null) {getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu = myToolbar.inflateMenu(this, menu);
        final Menu men = menu;
        final MenuItem logout = menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(
                item -> logoutAndRefresh(men, logout));
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ApplySharedPref")
    public boolean logoutAndRefresh(Menu men, MenuItem logout) {
        Toast.makeText(self, "Bye, " + PreferenceManager.getDefaultSharedPreferences(self).getString("pref_userName", null),
                Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(self).edit();
        edit.putString("pref_userName", null);
        edit.commit();
        refreshData();
        men.findItem(R.id.login).setVisible(true);
        logout.setVisible(false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MyToolbar.LOGIN: {
                if (resultCode == AppCompatActivity.RESULT_OK && data.getBooleanExtra("isLoggedIn", false)) {
                    refreshData();
                    myToolbar.loginSuccess();
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshData() {
        HomeFragment home = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");
        home.refreshData();
    }


    @Override
    public void onHomeFragmentInteraction(@NonNull Uri uri, Object data) {
        if(uri.getPath()==null){
            return;
        }
        String[] segments = uri.getPath().split("/");
        if (segments.length > 2) {
            switch (segments[2]) {
                case "displayBook": {
                    Book b = (Book) data;
                    BookDetailsFragment bookDetailsFragment = (BookDetailsFragment) getSupportFragmentManager().findFragmentByTag("DETAILS");
                    bookDetailsFragment.setBook(b);
                    swapFragmentTo(bookDetailsFragment, false);
                    break;
                }
            }
        }
    }

    @Override
    public void setHomeIcon() {
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger_menu_icon);

            categoriesDrawer = findViewById(R.id.drawer_layout);
            myToolbar.setNavigationOnClickListener(v -> activateDrawer());
        }
    }

    private void activateDrawer() {
        if (!categoriesDrawer.isDrawerOpen(GravityCompat.START)) {
            categoriesDrawer.openDrawer(GravityCompat.START);
        } else {
            categoriesDrawer.closeDrawer(GravityCompat.END);
        }
    }

    private void swapFragmentTo(Fragment fragment, boolean isBack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        Fragment current;
        for (Fragment f : fragments) {
            if(!f.isDetached()){
                current = f;
                ft.detach(f);
                if(!isBack && current!=fragment){
                    ft.addToBackStack(f.getTag());
                    setHomeAsUp();
                }
            }
        }
        ft.attach(fragment);
        ft.commit();
    }
    public void swapFragmentTo(String tag, boolean isBack) {
        FragmentManager fm = getSupportFragmentManager();
        swapFragmentTo(fm.findFragmentByTag(tag),isBack);
    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        for (Fragment f : fragments) {
            if(!f.isDetached()){
                if(f.getTag().equals("HOME")){
                    finish();
                }
                ft.detach(f);
            }
        }
        fm.popBackStack();
        //fm.popBackStackImmediate();
        ft.commit();
        if(fm.getBackStackEntryCount()==0) {
            setHomeIcon();
        }
    }

    @Override
    public void onBookDetailsInteraction(Uri uri) {

    }

    @Override
    public void onAddToCartListener(Book book, boolean isBuyNow) {

    }

    @Override
    public void setHomeAsUp() {
        myToolbar.setNavigationOnClickListener((v) -> onBackPressed());
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }
    }

    @Override
    public void onListFragmentInteraction(Book item) {
        BookOrderDialogBuilder.showOrderDialog(this, item, this, findViewById(R.id.page_content), new BookOrderDialogBuilder.OnBookOrderListener() {
            @Override
            public void onBuyNow() {
                swapFragmentTo("BASKET", false);
            }

            @Override
            public void onAddToBasket() {
                findViewById(R.id.filter).callOnClick();
            }
        });
    }

    @Override
    public void onDisplayDetailsListener(Book book) {
        BookDetailsFragment bdf = (BookDetailsFragment) fragments.get("DETAILS");
        bdf.setBook(book);
        swapFragmentTo("DETAILS", false);
    }

    @Override
    public void displayBigGallery(Book book) {
        BigGalleryFragment bdf = (BigGalleryFragment) fragments.get("GALLERY_FRAG");
        bdf.setBook(book);
        swapFragmentTo("GALLERY_FRAG", false);
    }

    @Override
    public void onCategoryClick(Category item) {
        filtersAdapter.reset();
        filtersAdapter.selectCategory(item);
        filtersAdapter.notifyDataSetChanged();
        findViewById(R.id.filter).callOnClick();
    }

    @Override
    public void displayBasket() {
        swapFragmentTo("BASKET", false);
    }


    public void openDrawer(View v){
        if (!categoriesDrawer.isDrawerOpen(GravityCompat.START)) {
            categoriesDrawer.openDrawer(GravityCompat.START);
        } else {
            categoriesDrawer.closeDrawer(GravityCompat.END);
        }
    }
}
