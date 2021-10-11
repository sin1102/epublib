package com.example.epub.Model;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Book {
    private  int resourceId;


    public Book (int resourceID){
        this.resourceId = resourceId;

    }

    public int getResourceId(){
        return resourceId;
    }

    public void setResourceId(int resourceId)   {
        this.resourceId = resourceId;
    }


}
