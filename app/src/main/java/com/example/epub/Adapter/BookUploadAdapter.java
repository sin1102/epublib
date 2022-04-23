package com.example.epub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.epub.R;
import com.example.epub.ReadBook.BookModel;
import com.example.epub.Display.EditUploadedBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookUploadAdapter extends RecyclerView.Adapter<BookUploadAdapter.BookUploadViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private FirebaseStorage storageReference;
    private DatabaseReference databaseReference;

    Context context;
    List<BookModel> bookUploadList;

    public BookUploadAdapter(Context context, List<BookModel> bookUploadList) {
        this.context = context;
        this.bookUploadList = bookUploadList;
    }

    public void setDataList(List<BookModel> list){
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

        BookModel bookUpload = bookUploadList.get(position);
        if (bookUpload == null){
            return;
        }

        Picasso.get().load(bookUpload.getBookCover()).into(holder.image_book_upload);
        holder.title_book_upload.setText(bookUpload.getBookTitle());
        holder.author_book_upload.setText(bookUpload.getBookAuthor());

        holder.delete_uploaded_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bookUploadList.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
                storageReference = FirebaseStorage.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BookModel temp = new BookModel();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            temp = child.getValue(BookModel.class);
                            if (temp.getBookURL().equals(bookUpload.getBookURL())) {
                                StorageReference img = storageReference.getReferenceFromUrl(bookUpload.getBookCover());
                                StorageReference epub = storageReference.getReferenceFromUrl(bookUpload.getBookURL());
                                img.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        epub.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                databaseReference.child(child.getKey()).removeValue();
                                                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.edit_uploaded_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, EditUploadedBookActivity.class);
                it.putExtra("book", bookUpload);
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
