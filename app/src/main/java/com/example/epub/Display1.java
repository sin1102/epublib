package com.example.epub;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Display1 extends AppCompatActivity{

    //RecyclerView Minh
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvCategory = findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(Display1.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
    }

    private List<Category> getListCategory(){
        List<Category> listCategory = new ArrayList<>();

        List<Book> listBook = new ArrayList<>();
        listBook.add(new Book(R.drawable.img));
        listBook.add(new Book(R.drawable.img_1));
        listBook.add(new Book(R.drawable.img_2));
        listBook.add(new Book(R.drawable.img_3));
        listBook.add(new Book(R.drawable.img_4));
        listBook.add(new Book(R.drawable.img_5));
        listBook.add(new Book(R.drawable.img_6));
        listBook.add(new Book(R.drawable.img_7));
        listBook.add(new Book(R.drawable.img_8));
        listBook.add(new Book(R.drawable.img_9));

        listCategory.add(new Category("New Books", listBook));
        listCategory.add(new Category("English Books", listBook));
        listCategory.add(new Category("Vietnamese Books", listBook));

        return listCategory;
    }

    //Hàm mở dialog
    private void openFeedbackDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.book_information);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        Button btnDownload = dialog.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Display1.this, "Đây là nút download", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    //Các hàm bắt sụ kiện image button
    public void image(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image1(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image2(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image3(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image5(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image6(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image7(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image8(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }

    public void image9(View view) {
        openFeedbackDialog(Gravity.CENTER);
    }
}