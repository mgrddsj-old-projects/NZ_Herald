package com.jesse.nzherald;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Todo Implement methods required
//onCreateViewHolder()
//onBindViewHolder
//getItemCount
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> implements Filterable
{
    List<Article> articleList;
    List<Article> articleListFull;
    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_player_view, parent, false);

        //Im gonna send my view group to my view holder
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.photo.setImageResource(articleList.get(position).getImageResource()); //TODO get image using Picasso
        holder.title.setText(articleList.get(position).getTitle());
        holder.author.setText(articleList.get(position).getAuthor() + "");
        holder.date.setText(articleList.get(position).getDate());
        holder.description.setText(articleList.get(position).getDescription()+"");
    }

    @Override
    public int getItemCount()
    {
        return articleList.size();
    }

    // Todo implement ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView photo;
        public TextView title;
        public TextView author;
        public TextView date;
        public TextView description;
        // get references to each of the views in the single_item.xml
        // Todo implement constructor
        private MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
        }
    }

    public NewsAdapter(List list)
    {
        articleList = list;
        articleListFull = new ArrayList<>(articleList);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Article> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(articleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Article item : articleListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articleList.clear();
            articleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}

