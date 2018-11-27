package com.semestr2.bartek.androidzadanie1;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.semestr2.bartek.androidzadanie1.categories.CategoriesArrayAdapter;
import com.semestr2.bartek.androidzadanie1.categories.Category;
import com.semestr2.bartek.androidzadanie1.fragments.HomeFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteractionListener{
    final private MainActivity self = this;
    private DrawerLayout categoriesDrawer;
    private MyToolbar myToolbar;
    private DatabaseAccess databaseAccess;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();


        setupToolbar();
        loadCategoriesList(databaseAccess);
        homeFragment = new HomeFragment();
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.add(R.id.page_content, homeFragment);
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
}
