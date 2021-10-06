package com.example.epub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

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


        try {
            List<User> bookList = new ArrayList<>();
            bookList = getListUser();
            mUserAdapter.setData(bookList);
        } catch (Exception e) {
             Log.v("Err", e.getMessage());
        }
        rcvUser.setAdapter(mUserAdapter);
    }

    private List<User> getListUser() throws Exception {
        List<User> list = new ArrayList<>();
        File dir = Environment.getExternalStorageDirectory();
        Search_Dir(dir, list);

        return list;
    }

    public void Search_Dir(File dir, List<User> list) throws Exception {
        InputStream inputStream = null;
        String epubExt = ".epub";
        File fileList[] = dir.listFiles();
        if (fileList != null) {
            for(int i = 0; i < fileList.length; i++) {
                //if (fileList[i].getName() == "Download") {
                //    Search_Dir(fileList[i], list);
                //} else {
                    if (fileList[i].getName().endsWith(epubExt)) {
                        inputStream = new BufferedInputStream(new FileInputStream(fileList[i].getPath()));
                        Book mybook = new EpubReader().readEpub(inputStream);
                        Bitmap cover = BitmapFactory.decodeStream(mybook.getCoverImage().getInputStream());
                        list.add(new User(cover, mybook.getTitle(), fileList[i].getPath()));
                    }
                //}
            }
        }
    }

}