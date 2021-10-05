package com.example.epub;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {
    TextView txtBook;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        String str = null;
        TextView textView = findViewById(R.id.txtContent);
        ImageView imageView = findViewById(R.id.imgTest);

        InputStream inputStream = null;

        try {
            // find InputStream for book
            //InputStream epubInputStream = assetManager
              //      .open("testbook.epub");
            new BufferedInputStream(new FileInputStream("/sdcard/"));


            // Load Book from inputStream
            Book book = null;
            final int REQUEST_EXTERNAL_STORAGE = 1;
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                //downloadFile();
                /**
                 * Write the code you want to execute here
                 */
                book = (new EpubReader()).readEpub(inputStream);
            }
            str = book.getSpine().toString();

            // Log the book's authors
            Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());

            // Log the book's title
            Log.i("epublib", "title: " + book.getTitle());

            // Log the book's coverimage property
            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
                    .getInputStream());
            Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "
                    + coverImage.getHeight() + " pixels");

            // Log the tale of contents
            logTableOfContents(book.getTableOfContents().getTocReferences(), 0);
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }
        textView.setText(str);
    }

    /**
     * Recursively Log the Table of Contents
     *
     * @param tocReferences
     * @param depth
     */
    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return;
        }
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            Log.w("epublib", tocString.toString());
            logTableOfContents(tocReference.getChildren(), depth + 1);
        }

    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };




}