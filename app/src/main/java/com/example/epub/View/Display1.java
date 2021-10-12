package com.example.epub.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.epub.Testt.TesrProcessbar;
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
    private List<BookModel> bookList;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    ProgressDialog progressDialog;
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

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        rcvCategory = findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                BookModel book = new BookModel();
//                bookList = new ArrayList<>();
//                for (DataSnapshot temp : snapshot.getChildren()) {
//                    book = temp.getValue(BookModel.class);
//                    bookList.add(book);
//                }
//                categoryAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent();
//                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(chooseFile, 2);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent = Intent.createChooser(intent, "Choose Books");
                startActivityForResult(intent, 2);

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

    private List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(R.id.image));
        bookList.add(new Book(R.id.image1));
        bookList.add(new Book(R.id.image2));
        bookList.add(new Book(R.id.image3));
        bookList.add(new Book(R.id.image6));
        bookList.add(new Book(R.id.image5));
        bookList.add(new Book(R.id.image7));
        bookList.add(new Book(R.id.image8));
        bookList.add(new Book(R.id.image9));

        listCategory.add(new Category("New Books", bookList));
        listCategory.add(new Category("English Books", bookList));
        listCategory.add(new Category("Vietnamese Books", bookList));

        return listCategory;
    }

    //Hàm mở dialog
    private void openFeedbackDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.book_information);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
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
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        bookCover = dialog.findViewById(R.id.imgCover);
        bookTitle = dialog.findViewById(R.id.txtTitleUp);
        bookAuthor = dialog.findViewById(R.id.edtAuthorUp);
        bookGenre = dialog.findViewById(R.id.txtGenreUp);
        bookLanguage = dialog.findViewById(R.id.txtLanguageUp);

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
        //pgBar = findViewById(R.id.pgBar);
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

        StorageReference fileRef = storageReference.child(bookDir[bookDir.length - 1]);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookAuthor(bookAuthor.getText().toString());
                        book.setBookTitle(bookTitle.getText().toString());
                        book.setBookGenre(bookGenre.getText().toString());
                        book.setBookLanguage(bookLanguage.getText().toString());
                        book.setBookURL(uri.toString());
                        String bookID = databaseReference.push().getKey();
                        databaseReference.child(bookID).setValue(book);
                        Toast.makeText(Display1.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                        Log.w("success", "upload success!");
                        //pgBar.setProgress(0);

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress  = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pgBar.setProgress((int)progress);
                progressDialog= new ProgressDialog(Display1.this);
                progressDialog.setTitle("Title");
                progressDialog.setMax(100);
                progressDialog.setMessage("Progress Bar");
                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
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