package com.example.epub.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epub.Control.Login;
import com.example.epub.R;
import com.example.epub.View.Display1;
import com.example.epub.View.Library;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SideBar extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

//    private static final int HOME = 0;
//    private static final int LIBRARY = 1;
//
//    private int mCurrent = HOME;


    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;

//     ImageView imageUser;
//     EditText txtUsername;
//     EditText txtEmail;
//
//    StorageReference storageReference;
//
//    FirebaseAuth fAuth;
//    FirebaseFirestore fStore;
//    FirebaseUser fUser;
//
//    String userID;

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

//        imageUser =  (ImageView) mDrawerLayout.findViewById(R.id.imageUser);
//        txtUsername = navigationView.findViewById(R.id.txtUsername);


//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference();
//        fUser = fAuth.getCurrentUser();
//        userID = fAuth.getCurrentUser().getUid();

//        StorageReference profileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid()+ "/profile.jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(imageUser);
//            }
//        });

//        DocumentReference documentReference = fStore.collection("Users").document(userID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                txtUsername.setText(value.getString("fullname"));
//            }
//        });

        navigationView.setNavigationItemSelectedListener(this);

        //replaceFragment(new HomeFrg());
        navigationView.getMenu().findItem(R.id.nav_Home).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_Home);






    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_Home) {
//            Intent it = new Intent(this, Display1.class);
//            startActivity(it);
//        }
//        else if (id == R.id.nav_Library){
//            Intent it = new Intent(this, Library.class);
//            startActivity(it);
//        }else  if (id == R.id.nav_Log_Out){
//
//        }
//        else if (id == R.id.nav_My_Profile) {
//            Intent it = new Intent(this, Profile.class);
//            startActivity(it);
//        }
//        else if (id == R.id.nav_Change_Password) {
//
//        }
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

//    private void replaceFragment(Fragment fragment){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_Home:
                Intent it = new Intent(this, Display1.class);
                startActivity(it);
                break;
            case R.id.nav_Library:
                Intent t = new Intent(this, Library.class);
                startActivity(t);
                break;
            case R.id.nav_My_Profile:
                Intent myProfile = new Intent(this, Profile.class);
                startActivity(myProfile);
                break;
            case R.id.nav_Change_Password:
                Intent intent = new Intent(this, ChangePassword.class);
                startActivity(intent);
                break;
            case R.id.nav_Log_Out:

                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();

                startActivity(new Intent(this, Login.class));
                FirebaseAuth.getInstance().signOut();
                finish();

            default:
                return true;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}