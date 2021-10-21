package com.example.epub.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar progressBar;
    private EditText txtEmail;
    private Button btnSendEmail;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtEmail = (EditText) findViewById(R.id.txtEmail);

        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        fAuth = FirebaseAuth.getInstance();

    }

    private void resetPassword() {
        String email = txtEmail.getText().toString().trim();

        if(email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        closeKeyboard();

        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(ForgotPassword.this, Login.class));
                    Toast.makeText(ForgotPassword.this, "Email has been sent. Please check your email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Something wrong! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSendEmail:
                resetPassword();
                break;
        }
    }
}