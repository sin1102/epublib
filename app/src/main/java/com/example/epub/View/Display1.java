package com.example.epub.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.Adapter.CategoryAdapter;
import com.example.epub.Model.BookModel;
import com.example.epub.Model.Category;
import com.example.epub.Model.Book;
import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;

public class Display1 extends SideBar {
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    EditText bookAuthor;
    EditText bookGenre;
    EditText bookLanguage;
    ImageView bookCover;
    TextView bookTitle;
    ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display1);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_display1, null, false);
        mDrawerLayout.addView(v, 0);

        rcvCategory = findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent();
                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(chooseFile, 2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            try {
                openUpLoad(Gravity.CENTER, data.getData().getPath(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
    //Show popup UpLoad
    private void openUpLoad(int gravity, String bookDir, Uri uri) throws Exception {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_up_load_book);
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
        bookCover = dialog.findViewById(R.id.imgCover);
        bookTitle = dialog.findViewById(R.id.txtTitleUp);
        bookAuthor = dialog.findViewById(R.id.edtAuthorUp);
        bookGenre = dialog.findViewById(R.id.txtGenreUp);
        bookLanguage = dialog.findViewById(R.id.txtLanguage);

        InputStream bookStream = new BufferedInputStream(new FileInputStream(bookDir));
        nl.siegmann.epublib.domain.Book epub = new EpubReader().readEpub(bookStream);
        bookTitle.setText(epub.getTitle());
        Bitmap coverImage = BitmapFactory.decodeStream(epub.getCoverImage()
                .getInputStream());
        bookCover.setImageBitmap(coverImage);

        Button btnUpload = dialog.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadToFirebase(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    //Upload to FireBase

    private void uploadToFirebase(Uri uri) throws Exception {
        BookModel book = new BookModel();
        String[] bookDir = uri.getPath().split("/");
        StorageReference imgRef = storageReference.child(bookDir[bookDir.length - 1] + ".jpg");
        InputStream inputStream = new BufferedInputStream(new FileInputStream(uri.getPath()));
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

        StorageReference fileRef = storageReference.child(bookDir[bookDir.length - 1] + ".epub");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookAuthor(bookAuthor.getText().toString());
                        book.setBookTitle(bookTitle.getText().toString());
                        book.setBookGenre(bookGenre.getText().toString());
                        book.setBookURL(uri.toString());
                        String bookID = databaseReference.push().getKey();
                        databaseReference.child(bookID).setValue(book);
                        //pgBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Display1.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                        Log.w("success", "upload success!");
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //pgBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Display1.this, "Uploading faile!", Toast.LENGTH_SHORT).show();
                Log.w("fail", "upload failed!");
            }
        });


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