package com.example.epub.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.epub.Adapter.CategoryAdapter;
import com.example.epub.Adapter.MainAdapter;
import com.example.epub.Model.Book;
import com.example.epub.Model.Category;
import com.example.epub.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView1, recyclerView2,  recyclerView3;
    List<Book> bookList;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView3 = findViewById(R.id.recycler_view3);
        bookList = new ArrayList<>();

        bookList.add(new Book(R.drawable.img, "Book 1") );
        bookList.add(new Book(R.drawable.img_1,"Book 2"));
        bookList.add(new Book(R.drawable.img_2, "Book 3"));
        bookList.add(new Book(R.drawable.img_3, "Book 4"));
        bookList.add(new Book(R.drawable.img_4, "Book 5"));
        bookList.add(new Book(R.drawable.img_5, "Book 6"));
        bookList.add(new Book(R.drawable.img_6, "Book 7"));
        bookList.add(new Book(R.drawable.img_7, "Book 8"));
        bookList.add(new Book(R.drawable.img_8, "Book 9"));
        bookList.add(new Book(R.drawable.img_9, "Book 10"));
        categoryList = new ArrayList<>();

        categoryList.add(new Category(R.drawable.img, "Book 1", "Bao ngu 1") );
        categoryList.add(new Category(R.drawable.img_1,"Book 2", "Bao ngu 2"));
        categoryList.add(new Category(R.drawable.img_2, "Book 3", "Bao ngu 3"));
        categoryList.add(new Category(R.drawable.img_3, "Book 4", "Bao ngu 4"));
        categoryList.add(new Category(R.drawable.img_4, "Book 5", "Bao ngu 5"));
        categoryList.add(new Category(R.drawable.img_5, "Book 6", "Bao ngu 6"));
        categoryList.add(new Category(R.drawable.img_6, "Book 7", "Bao ngu 7"));
        categoryList.add(new Category(R.drawable.img_7, "Book 8", "Bao ngu 8"));
        categoryList.add(new Category(R.drawable.img_8, "Book 9", "Bao ngu 9"));
        categoryList.add(new Category(R.drawable.img_9, "Book 10", "Bao ngu 10"));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView3.setLayoutManager(layoutManager3);


//        MainAdapter mainAdapter1 = new MainAdapter(bookList, this);
//        recyclerView1.setAdapter(mainAdapter1);
//
//        MainAdapter mainAdapter2 = new MainAdapter(bookList, this);
//        recyclerView2.setAdapter(mainAdapter2);
//
//        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, this);
//        recyclerView3.setAdapter(categoryAdapter);
    }
}