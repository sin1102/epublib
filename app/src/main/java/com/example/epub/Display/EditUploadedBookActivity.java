package com.example.epub.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.R;
import com.example.epub.ReadBook.Book;
import com.example.epub.ReadBook.BookModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;

public class EditUploadedBookActivity extends AppCompatActivity {

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
    Button update;
    ProgressDialog progressDialog;
    boolean[] selectCourse;
    ArrayList<Integer> courseList = new ArrayList<>();
    String[] courseArray = {"Reference", "Novel", "Literature", "Horror", "Romance", "Science Fiction", "Fantasy", "Slice of life"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_uploaded_book);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        selectCard = findViewById(R.id.genreUp);
        tvCourses = findViewById(R.id.courseText);
        bookTitle = findViewById(R.id.txtTitleUp);
        author = findViewById(R.id.authorUp);
        content = findViewById(R.id.infoBook);
        language = findViewById(R.id.languageSpinner);
        cover = findViewById(R.id.imgCover);
        update = findViewById(R.id.btnUpdate);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter1);

        selectCourse = new boolean[courseArray.length];
        selectCard.setOnClickListener(v -> {
            showCoursesDialog();
        });

        Intent it = getIntent();
        BookModel book = (BookModel) it.getSerializableExtra("book");

        Picasso.get().load(book.getBookCover()).into(cover);
        bookTitle.setText(book.getBookTitle());
        author.setText(book.getBookAuthor());
        content.setText(book.getBookContent());
        if (book.getBookLanguage().toLowerCase().equals("english"))
            language.setSelection(1);
        else
            language.setSelection(0);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateToFirebase(book);
            }
        });

    }

    void showCoursesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditUploadedBookActivity.this);

        builder.setTitle("Select Courses");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(courseArray, selectCourse, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    courseList.add(which);
                } else {
                    courseList.remove(which);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //create string builder
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < courseList.size(); i++) {
                    stringBuilder.append(courseArray[courseList.get(i)]);

                    //check condition
                    if (i != courseList.size() - 1) {

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
                for (int i = 0; i < selectCourse.length; i++) {

                    selectCourse[i] = false;

                    courseList.clear();
                    tvCourses.setText("");
                }
            }
        });

        builder.show();
    }

    private void updateToFirebase(BookModel bm) {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel temp = new BookModel();
                for (DataSnapshot child : snapshot.getChildren()) {
                    temp = child.getValue(BookModel.class);
                    if (temp.getBookURL().equals(bm.getBookURL())) {
                        String ID = child.getKey();
                        bm.setBookAuthor(author.getText().toString());
                        bm.setBookTitle(bookTitle.getText().toString());
                        bm.setBookGenre(genre);
                        bm.setBookLanguage(language.getSelectedItem().toString());
                        bm.setBookContent(content.getText().toString());
                        bm.setuID(fUser.getUid());
                        databaseReference.child(ID).setValue(bm);
                        Toast.makeText(EditUploadedBookActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                        Log.w("success", "upload success!");
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}