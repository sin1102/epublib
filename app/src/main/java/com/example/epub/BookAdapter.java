package com.example.epub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> mBook;

    public  void  setData(List<Book> list){
        this.mBook = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBook.get(position);
        if(book == null){
            return;
        }
        holder.imgBook.setImageResource(book.getResourceId());
    }

    @Override
    public int getItemCount() {
        if (mBook != null)
            return mBook.size();
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgBook;

        public BookViewHolder(@NonNull View itemView){
            super(itemView);

            imgBook = itemView.findViewById(R.id.img_book);
        }
    }
}
