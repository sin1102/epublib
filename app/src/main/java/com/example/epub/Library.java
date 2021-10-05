package com.example.epub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Library extends AppCompatActivity {


    private RecyclerView rcvUser;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        rcvUser = findViewById(R.id.rcv_user);
        mUserAdapter = new UserAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcvUser.setLayoutManager(gridLayoutManager);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.LayoutHeader)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUserAdapter.setData(getListUser());
        rcvUser.setAdapter(mUserAdapter);

    }

    private List<User> getListUser(){
        List<User> list = new ArrayList<>();
        list.add(new User(R.drawable.img_1, "User 1"));
        list.add(new User(R.drawable.img_2, "User 2"));
        list.add(new User(R.drawable.img_3, "User 3"));
        list.add(new User(R.drawable.img_4, "User 4"));

        list.add(new User(R.drawable.img_1, "User 1"));
        list.add(new User(R.drawable.img_2, "User 2"));
        list.add(new User(R.drawable.img_3, "User 3"));
        list.add(new User(R.drawable.img_4, "User 4"));

        list.add(new User(R.drawable.img_1, "User 1"));
        list.add(new User(R.drawable.img_2, "User 2"));
        list.add(new User(R.drawable.img_3, "User 3"));
        list.add(new User(R.drawable.img_4, "User 4"));

        return list;
    }

}