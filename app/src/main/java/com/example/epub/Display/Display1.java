package com.example.epub.Display;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.Adapter.CategoryAdapter;
import com.example.epub.Adapter.MainAdapter;
import com.example.epub.ReadBook.BookModel;
import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;

public class Display1 extends SideBar {

    private List<BookModel> bookList;
    private SearchView searchView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ImageButton imageButton;

    ProgressDialog progressDialog;
    EditText bookAuthor;
//    EditText bookGenre;
//    EditText bookLanguage;
    Spinner bookLanguage, bookGenre;
    ImageView bookCover;
    TextView bookTitle;
    RecyclerView recyclerView1, recyclerView2,  recyclerView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display1);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_display1, null, false);
        mDrawerLayout.addView(v, 0);

        bookList = new ArrayList<>();
        List<BookModel> VNBook = new ArrayList<>();
        List<BookModel> ENBook = new ArrayList<>();
        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView3 = findViewById(R.id.recycler_view3);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView3.setLayoutManager(layoutManager3);

        MainAdapter mainAdapter1 = new MainAdapter(VNBook, this);
        recyclerView1.setAdapter(mainAdapter1);

        MainAdapter mainAdapter2 = new MainAdapter(ENBook, this);
        recyclerView2.setAdapter(mainAdapter2);

        CategoryAdapter categoryAdapter = new CategoryAdapter(bookList, this);
        recyclerView3.setAdapter(categoryAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel book = new BookModel();
                VNBook.clear();
                ENBook.clear();
                bookList.clear();
                for (DataSnapshot temp : snapshot.getChildren()) {
                    book = temp.getValue(BookModel.class);
                    bookList.add(book);
                    if (book.getBookLanguage().equals("Vietnamese"))
                        VNBook.add(book);
                    if (book.getBookLanguage().equals("English"))
                        ENBook.add(book);
                }
                mainAdapter1.notifyDataSetChanged();
                mainAdapter2.notifyDataSetChanged();
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent();
//                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(chooseFile, 2);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent = Intent.createChooser(intent, "Choose Books");
                startActivityForResult(intent, 2);
            }
        });
    }

    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            try {
//                String[] path = data.getData().getPath().split(":");
//                Toast.makeText(this, path[1], Toast.LENGTH_SHORT).show();
//                openUpLoad(Gravity.CENTER, path[1], data.getData());
                Intent it = new Intent(this, Uploadbook.class);
                it.putExtra("bookuri", data.getData().toString());
                startActivity(it);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search_action){
            Intent intent = new Intent(Display1.this, SearchDisplay.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}