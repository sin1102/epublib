package com.example.epub.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.R;
import com.example.epub.ReadBook.BookModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import nl.siegmann.epublib.epub.EpubReader;
import ru.bartwell.exfilepicker.ui.dialog.SortingDialog;

public class Uploadbook extends AppCompatActivity {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    FirebaseAuth fAuth;
    FirebaseUser fUser;


    String genre;
    MaterialCardView selectCard;
    TextView tvCourses, bookTitle;
    EditText author, content;
    Spinner language;
    ImageView cover;
    Button upload;
    ProgressDialog progressDialog;
    boolean [] selectCourse;
    ArrayList<Integer> courseList = new ArrayList<>();
    String [] courseArray = {"Reference","Novel","Literature","Horror","Romance","Science Fiction","Fantasy","Slice of life"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadbook);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        // initialize all views
        selectCard = findViewById(R.id.txtGenreUp);
        tvCourses = findViewById(R.id.tvCourse);
        bookTitle = findViewById(R.id.txtTitleUp);
        author = findViewById(R.id.edtAuthorUp);
        content = findViewById(R.id.txtInfoBook);
        language = findViewById(R.id.spinner1);
        cover = findViewById(R.id.imgCover);
        upload = findViewById(R.id.btnUpload);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.language, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter1);

        selectCourse = new boolean[courseArray.length];
        selectCard.setOnClickListener(v ->{
            showCoursesDialog();
        });

        Intent it = getIntent();
        Uri book = Uri.parse(it.getStringExtra("bookuri"));

        InputStream bookStream = null;
        try {
            bookStream = getContentResolver().openInputStream(book);
            nl.siegmann.epublib.domain.Book epub = new EpubReader().readEpub(bookStream);
            bookTitle.setText(epub.getTitle());
            Bitmap coverImage = BitmapFactory.decodeStream(epub.getCoverImage()
                    .getInputStream());
            cover.setImageBitmap(coverImage);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        uploadToFirebase(book);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void showCoursesDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Uploadbook.this);

        builder.setTitle("Select Courses");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(courseArray, selectCourse, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    courseList.add(which);
                }else {
                 courseList.remove(which);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //create string builder
                StringBuilder stringBuilder = new StringBuilder();
                for (int i=0; i < courseList.size(); i++){
                    stringBuilder.append(courseArray[courseList.get(i)]);

                    //check condition
                    if (i != courseList.size() -1){

                        //when i value not equal to course list size
                        //then add a comma
                        stringBuilder.append(", ");
                    }

                    //setting selected course to textview
                    tvCourses.setText(stringBuilder.toString());
                    genre = stringBuilder.toString();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //clearing all selected course on clear all click
                for (int i = 0; i< selectCourse.length; i++){

                    selectCourse[i] = false;

                    courseList.clear();
                    tvCourses.setText("");
                }
            }
        });

        builder.show();
    }

    private void uploadToFirebase(Uri uri) throws Exception {
        progressDialog= new ProgressDialog(Uploadbook.this);
        progressDialog.setTitle("Upload book");
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();


        BookModel book = new BookModel();
        String[] bookDir = uri.getPath().split("/");
        Log.w("failed", bookDir[bookDir.length - 1]);
        StorageReference imgRef = storageReference.child(bookDir[bookDir.length - 1] + ".jpg");
        InputStream inputStream = getContentResolver().openInputStream(uri);
        nl.siegmann.epublib.domain.Book temp = new EpubReader().readEpub(inputStream);
        imgRef.putBytes(temp.getCoverImage().getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookCover(uri.toString());
                        Log.w("success", "upload img success");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("fail", "load img fail");
            }
        });

        StorageReference fileRef = storageReference.child(bookDir[bookDir.length - 1]);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookAuthor(author.getText().toString());
                        book.setBookTitle(bookTitle.getText().toString());
                        book.setBookGenre(genre);
                        book.setBookLanguage(language.getSelectedItem().toString());
                        book.setBookURL(uri.toString());
                        book.setBookContent(content.getText().toString());
                        book.setuID(fUser.getUid());
                        String bookID = databaseReference.push().getKey();
                        databaseReference.child(bookID).setValue(book);
                        Toast.makeText(Uploadbook.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                        Log.w("success", "upload success!");
                        progressDialog.dismiss();
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress  = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress((int)progress);
                    }
                }).start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Uploadbook.this, "Uploading faile!", Toast.LENGTH_SHORT).show();
                Log.w("fail", "upload failed!");
            }
        });
    }

}