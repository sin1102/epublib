package com.example.epub.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUsername, txtEmail;
    private ImageView imageUser;
    private TextView btnUpdate;
    private ImageView btnTurnback;
    private ProgressBar progressBar;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        imageUser = (ImageView) findViewById(R.id.imageUser);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnUpdate = (TextView) findViewById(R.id.btnUpdate);
        btnTurnback = (ImageView) findViewById(R.id.btnTurnback);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        displayProfile();

        btnUpdate.setOnClickListener(this);
        imageUser.setOnClickListener(this);
        btnTurnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTurnback:
                startActivity(new Intent(Profile.this, Display1.class));
                finishAffinity();
                break;
            case R.id.btnUpdate:
                updateProfile();
                break;
            case R.id.imageUser:
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 10);
                break;
        }
    }

    private void updateProfile(){
        String name = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        if(name.isEmpty()){
            txtUsername.setError("Please enter your full name");
            txtUsername.requestFocus();
        }
        else
        {
            DocumentReference documentReference = fStore.collection("Users").document(userID);
            Map<String, Object> edited = new HashMap<>();
            edited.put("fullname", name);
            edited.put("email", email);
            progressBar.setVisibility(View.VISIBLE);
            closeKeyboard();
            documentReference.set(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Profile.this, "Your profile has been updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void displayProfile(){
        StorageReference profileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageUser);
            }
        });

        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                txtUsername.setText(value.getString("fullname"));
                txtEmail.setText(value.getString("email"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                uploadImage(imageUri);
            }
        }
    }

    //Upload image from this into firebase
    private void uploadImage(Uri imageUri) {
        StorageReference fileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageUser);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    //Close keyboard when click method
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}