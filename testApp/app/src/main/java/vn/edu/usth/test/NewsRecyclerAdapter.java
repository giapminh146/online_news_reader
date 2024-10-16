package vn.edu.usth.test;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.test.Models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    List<Article> articleList;

    NewsRecyclerAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Call the frame that will show in the main screen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle()); //Sets the article's title in the TextView
        holder.sourceTextView.setText(article.getSource().getName()); //Display the source of the article
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.ic_email) //Image will show when missing or fails to load
                .placeholder(R.drawable.ic_email)
                .into(holder.imageView);

        holder.itemView.setOnClickListener((v -> {
            Intent intent = new Intent(v.getContext(), Reading1Activity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        }));

        if (article.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
        }

        // Set the click listener for the bookmark button
        holder.bookmarkButton.setOnClickListener(v -> {
            article.setBookmarked(!article.isBookmarked());
            notifyItemChanged(position);
            // Toggle bookmark state
            if (article.isBookmarked()) {
                SavedArticlesManager.addSavedArticle(article);
            } else {
                SavedArticlesManager.removeSavedArticle(article);
            }
        });
    }

    void updateData(List<Article> data) {
        articleList.clear();
        articleList.addAll(data);
    }
    @Override
    public int getItemCount() {
        return articleList.size(); //Tells the adapter how many items to display in RecycleView
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView;
        ImageView imageView;
        ImageButton bookmarkButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            sourceTextView = itemView.findViewById(R.id.text_source);
            imageView = itemView.findViewById(R.id.img_headline);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
        }
    }
}
