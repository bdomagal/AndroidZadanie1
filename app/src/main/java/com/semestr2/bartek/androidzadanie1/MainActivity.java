package com.semestr2.bartek.androidzadanie1;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.categories.CategoriesArrayAdapter;
import com.semestr2.bartek.androidzadanie1.fragments.BookDetailsFragment;
import com.semestr2.bartek.androidzadanie1.fragments.HomeFragment;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteractionListener, OnFragmentInteractionListener {
    final private MainActivity self = this;
    private DrawerLayout categoriesDrawer;
    private MyToolbar myToolbar;
    private DatabaseAccess databaseAccess;
    private HomeFragment homeFragment;
    private FrameLayout frame;
    private BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
    Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        frame = findViewById(R.id.page_content);
        setupToolbar();
        loadCategoriesList(databaseAccess);
        homeFragment = new HomeFragment();
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.page_content, homeFragment);
        fm.add(R.id.page_content, bookDetailsFragment);
        fm.detach(bookDetailsFragment);
        //fm.detach(homeFragment);
        fm.commit();
        databaseAccess.close();

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
        categoriesList.setOnItemClickListener(new CategoriesArrayAdapter.OnItemClickListener());
        categoriesList.setAdapter(new CategoriesArrayAdapter(this, databaseAccess.getCategories()));
    }

    private void setupToolbar() {
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.burger_menu_icon);

        categoriesDrawer = findViewById(R.id.drawer_layout);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!categoriesDrawer.isDrawerOpen(GravityCompat.START)){
                    categoriesDrawer.openDrawer(GravityCompat.START);
                }else{
                    categoriesDrawer.closeDrawer(GravityCompat.END);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu = myToolbar.inflateMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("paodgpaogapg");
        switch (requestCode){
            case MyToolbar.LOGIN : {
                if(resultCode == AppCompatActivity.RESULT_OK && data.getBooleanExtra("isLoggedIn", false)){
                    myToolbar.loginSuccess();
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHomeFragmentInteraction(Uri uri, Object data) {
        String[] segments = uri.getPath().split("/");
        System.out.println(segments[2]);
        if(segments.length>2){
            switch (segments[2]){
                case "displayBook" : {
                    Book b = (Book) data;
                    boolean isNew = false;
                    if(bookDetailsFragment==null){
                        bookDetailsFragment = new BookDetailsFragment();
                    }
                    bookDetailsFragment.setBook(b);
                    swapFragmentTo(bookDetailsFragment, isNew);
                    break;
                }
            }
        }
    }

    @Override
    public void setHomeIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger_menu_icon);

        categoriesDrawer = findViewById(R.id.drawer_layout);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!categoriesDrawer.isDrawerOpen(GravityCompat.START)){
                    categoriesDrawer.openDrawer(GravityCompat.START);
                }else{
                    categoriesDrawer.closeDrawer(GravityCompat.END);
                }
            }
        });
    }

    private void swapFragmentTo(BookDetailsFragment bookDetailsFragment, boolean isNew) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(homeFragment);
        if(isNew){
            ft.add(R.id.page_content, bookDetailsFragment);
        }else {
            ft.attach(bookDetailsFragment);
        }
        ft.addToBackStack(null);
        ft.commit();
        currentFragment = bookDetailsFragment;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(currentFragment!=null){
            ft.detach(currentFragment);
            currentFragment = null;
        }
        fm.popBackStackImmediate();
        ft.commit();
    }

    @Override
    public void onBookDetailsInteraction(Uri uri) {

    }

    @Override
    public void onAddToCartListener(Book book, boolean isBuyNow) {

    }

    @Override
    public void setHomeAsUp(){
        myToolbar.setNavigationOnClickListener((v)->onBackPressed());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
    }
}
