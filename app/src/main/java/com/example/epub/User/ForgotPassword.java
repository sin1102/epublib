package com.example.epub.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private EditText txtEmail;
    private Button btnSendEmail;
    private ImageButton btnBack;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(this);
    }

    private void showToast(String message){
        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                startActivity(new Intent(ForgotPassword.this, Login.class));
                finish();
                break;
            case R.id.btnSendEmail:
                resetPassword();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void resetPassword() {
        String email = txtEmail.getText().toString().trim();
        if(email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
        }else{
            closeKeyboard();
            progressBar.setVisibility(View.VISIBLE);
            fAuth = FirebaseAuth.getInstance();
            fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(ForgotPassword.this, Login.class));
                        finishAffinity();
                        showToast("Please check your email to change password");
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        showToast("Wrong email");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}