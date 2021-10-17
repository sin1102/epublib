package com.example.epub.Adapter;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epub.Model.BookModel;
import com.example.epub.Model.Category;
import com.example.epub.View.Display1;
import com.example.epub.R;
import com.example.epub.View.Download;
import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private List<BookModel> categories, categoriesList;
    private Context context;

    public CategoryAdapter(List<BookModel> categories, Context context) {
        this.categories = categories;
        this.categoriesList = categories;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    categories = categoriesList;
                }
                else{
                    List<BookModel> list = new ArrayList<>();
                    for (BookModel bookModel : categoriesList){
                        if (bookModel.getBookTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(bookModel);
                        }
                    }
                    categories = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = categoriesList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categories = (List<BookModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nTitle;
        ImageView nImage;
        TextView nGenre;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nTitle = itemView.findViewById(R.id.title_book_2);
            nImage = itemView.findViewById(R.id.img_category);
            nGenre = itemView.findViewById(R.id.genre);
            cardView = itemView.findViewById(R.id.verticalList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel category = categories.get(position);

        holder.nTitle.setText(category.getBookTitle());
        Picasso.get().load(category.getBookCover()).into(holder.nImage);
        holder.nGenre.setText(category.getBookGenre());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, Download.class);
                it.putExtra("BOOK", category);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }
}