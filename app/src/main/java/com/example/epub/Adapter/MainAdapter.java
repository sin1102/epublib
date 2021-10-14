package com.example.epub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epub.Model.Book;
import com.example.epub.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Book> books;
    private Context context;

    public MainAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        ImageView mImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.title_book);
            mImage = itemView.findViewById(R.id.image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.mTitle.setText(book.getTitle());
        holder.mImage.setImageResource(book.getImage());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
