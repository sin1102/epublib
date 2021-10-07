package com.example.epub;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class Display1 extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display1);

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