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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epub.Display.Display1;
import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private Button btnChangePassword;
    private ImageButton btnBack;
    private EditText txtNewPassword, txtConfirmPassword, txtCurrentPassword;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        txtCurrentPassword = (EditText) findViewById(R.id.txtCurrentPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, Display1.class));
                finishAffinity();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });

    }

    private void showToast(String message){
        Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
    }

    private void ChangePassword(){
        closeKeyboard();
        String newPassword = txtNewPassword.getText().toString().trim();
        String currentPassword = txtCurrentPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        if(currentPassword.isEmpty()){
            txtCurrentPassword.setError("Please enter your current password");
            txtCurrentPassword.requestFocus();
        } else if(newPassword.isEmpty()){
            txtNewPassword.requestFocus();
            txtNewPassword.setError("Please enter your new password");
        }else if(newPassword.length() < 6){
            txtNewPassword.requestFocus();
            txtNewPassword.setError("Your password must be bigger than 6 characters");
        }else if(confirmPassword.isEmpty()){
            txtConfirmPassword.setError("Please enter confirm password");
            txtConfirmPassword.requestFocus();
        }else if(!confirmPassword.equals(newPassword)){
            txtConfirmPassword.setError("Password doesn't match");
            txtConfirmPassword.requestFocus();
        }else{
            fAuth = FirebaseAuth.getInstance();
            user = fAuth.getCurrentUser();
            progressBar.setVisibility(View.VISIBLE);
            AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            showToast("Password has been updated successfully");
                            startActivity(new Intent(ChangePassword.this, Display1.class));
                            finishAffinity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            showToast("Something wrong");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    txtCurrentPassword.setError("Wrong current password");
                    txtCurrentPassword.requestFocus();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}