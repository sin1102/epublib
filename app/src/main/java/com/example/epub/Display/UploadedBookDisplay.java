package com.example.epub.Display;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.epub.ReadBook.BookModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadedBookDisplay extends SideBar {

    private List<BookModel> bookUploadList;
    private RecyclerView rcvUploadedBook;
    private BookUploadAdapter bookUploadAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activity_uploaded_book);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_uploaded_book, null, false);
        mDrawerLayout.addView(v, 1);

        rcvUploadedBook = (RecyclerView)findViewById(R.id.rcv_uploaded_book);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUploadedBook.setLayoutManager(linearLayoutManager);

        generateItem();
    }

    private void generateItem() {

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        List<BookModel> bookUploadList = new ArrayList<>();

        bookUploadAdapter = new BookUploadAdapter(this, bookUploadList);
        rcvUploadedBook.setAdapter(bookUploadAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel book = new BookModel();
                bookUploadList.clear();
                for (DataSnapshot temp : snapshot.getChildren()) {
                    book = temp.getValue(BookModel.class);
                    if (book.getuID().equals(fUser.getUid()))
                        bookUploadList.add(book);
                }
                bookUploadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
