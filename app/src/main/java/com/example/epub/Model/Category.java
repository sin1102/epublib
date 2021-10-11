package com.example.epub.Model;

import com.example.epub.Model.Book;

import java.util.List;

public class Category {

    private String nameCategory;
    private List<BookModel> books;

    public Category(String nameCategory, List<BookModel> books){
        this.nameCategory = nameCategory;
        this.books = books;
    }

    public String getNameCategory(){
        return nameCategory;
    }

    public void setNameCategory(String nameCategory){
        this.nameCategory = nameCategory;
    }

    public List<BookModel> getBooks(){
        return books;
    }

    public void setBooks(List<BookModel> books) {
        this.books = books;
    }
}
