package com.example.epub;

import android.graphics.Bitmap;

public class User {
    private Bitmap resouceImage;
    private String name;
    private String bookDir;


    public User(Bitmap resouceImage, String name, String bookDir) {
        this.resouceImage = resouceImage;
        this.name = name;
        this.bookDir = bookDir;
    }

    public String getBookDir() {
        return bookDir;
    }

    public void setBookDir(String bookDir) {
        this.bookDir = bookDir;
    }

    public Bitmap getResouceImage() {
        return resouceImage;
    }

    public void setResouceImage(Bitmap resouceImage) {
        this.resouceImage = resouceImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
