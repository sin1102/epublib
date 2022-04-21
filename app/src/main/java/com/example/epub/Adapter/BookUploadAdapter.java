package com.example.epub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.epub.Display.UploadedBookDisplay;
import com.example.epub.R;
import com.example.epub.UploadedBook.BookUpload;
import com.example.epub.UploadedBook.EditUploadedBookActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.collections.GroupingKt;

public class BookUploadAdapter extends RecyclerView.Adapter<BookUploadAdapter.BookUploadViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    Context context;
    List<BookUpload> bookUploadList;

    public BookUploadAdapter(Context context, List<BookUpload> bookUploadList) {
        this.context = context;
        this.bookUploadList = bookUploadList;
    }

    public void setDataList(List<BookUpload> list){
        this.bookUploadList = list;
        notifyDataSetChanged();
    }


    public class BookUploadViewHolder extends RecyclerView.ViewHolder {

        private SwipeRevealLayout swipeRevealLayout;
        private TextView delete_uploaded_book, edit_uploaded_book;

        private ImageView image_book_upload;
        private TextView title_book_upload;
        private TextView author_book_upload;

        public BookUploadViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            delete_uploaded_book = itemView.findViewById(R.id.delete_upload_book);
            edit_uploaded_book = itemView.findViewById(R.id.edit_upload_book);

            image_book_upload = itemView.findViewById(R.id.cart_image);
            title_book_upload = itemView.findViewById(R.id.cart_title);
            author_book_upload = itemView.findViewById(R.id.cart_author);
        }
    }

    @NonNull
    @Override
    public BookUploadAdapter.BookUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uploaded, parent, false);
        return new BookUploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookUploadAdapter.BookUploadViewHolder holder, int position) {

        BookUpload bookUpload = bookUploadList.get(position);
        if (bookUpload == null){
            return;
        }

        holder.image_book_upload.setImageResource(R.drawable.img);
        holder.title_book_upload.setText(bookUpload.getTitle());
        holder.author_book_upload.setText(bookUpload.getAuthor());

        holder.delete_uploaded_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookUploadList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.edit_uploaded_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, EditUploadedBookActivity.class);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookUploadList != null){
            return bookUploadList.size();
        }
        return 0;
    }
}
