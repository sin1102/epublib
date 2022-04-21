package com.example.epub.Display;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epub.Adapter.BookUploadAdapter;
import com.example.epub.R;
import com.example.epub.UploadedBook.BookUpload;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadedBookDisplay extends SideBar {

    private List<BookUpload> bookUploadList;
    private RecyclerView rcvUploadedBook;
    private BookUploadAdapter bookUploadAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_uploaded_book);

        rcvUploadedBook = (RecyclerView)findViewById(R.id.rcv_uploaded_book);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUploadedBook.setLayoutManager(linearLayoutManager);

        generateItem();
    }

    private void generateItem() {

        List<BookUpload> bookUploadList = new ArrayList<>();

        bookUploadList.add(new BookUpload("Book 1","Author 1", R.id.image));
        bookUploadList.add(new BookUpload("Book 2","Author 2", R.id.image));
        bookUploadList.add(new BookUpload("Book 3","Author 3", R.id.image));
        bookUploadList.add(new BookUpload("Book 4","Author 4", R.id.image));
        bookUploadList.add(new BookUpload("Book 5","Author 5", R.id.image));
        bookUploadList.add(new BookUpload("Book 6","Author 6", R.id.image));
        bookUploadList.add(new BookUpload("Book 7","Author 7", R.id.image));
        bookUploadList.add(new BookUpload("Book 8","Author 8", R.id.image));

        bookUploadAdapter = new BookUploadAdapter(this, bookUploadList);
        rcvUploadedBook.setAdapter(bookUploadAdapter);

    }
}
