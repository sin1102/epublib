package com.example.epub.Testt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.epub.R;

public class TesrProcessbar extends AppCompatActivity {
    Button btn;
    ProgressDialog progressDialog;
    private int progress=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tesr_processbar);
        btn = findViewById(R.id.Test);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog= new ProgressDialog(TesrProcessbar.this);
                progressDialog.setTitle("Title");
                progressDialog.setMax(100);
                progressDialog.setMessage("Progress Bar");
                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progress<100){
                            try {
                                Thread.sleep(200);
                                progress++;
                                progressDialog.setProgress(progress);
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}