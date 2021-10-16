package com.example.epub.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.epub.R;
import com.example.epub.View.Display1;
import com.example.epub.View.Library;
import com.google.android.material.navigation.NavigationView;

public class SideBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    private static final int FRAGMENT_HOME = 0;
//    private static final int FRAGMENT_LIBRARY = 1;
//
//    private int mCurrentFragment = FRAGMENT_HOME;


    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //replaceFragment(new HomeFrg());
        navigationView.getMenu().findItem(R.id.nav_Home).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_Home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_Home) {
                Intent it = new Intent(this, Display1.class);
                startActivity(it);
        }
        else if (id == R.id.nav_Library){
            Intent it = new Intent(this, Library.class);
            startActivity(it);

        }else  if (id == R.id.nav_Log_Out){

        }
        else if (id == R.id.nav_My_Profile) {
            Intent it = new Intent(this, Profile.class);
            startActivity(it);
        }
        else if (id == R.id.nav_Change_Password) {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
//
    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
//
//    private void replaceFragment(Fragment fragment){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.nav_Home:
//                Toast.makeText(this,"Home", Toast.LENGTH_SHORT).show();
//
//                break;
//
//        }
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }


}