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

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.usth.test.Models.Article;

public class SavedArticlesAdapter extends RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder> {
    private List<Article> savedArticles;

    public SavedArticlesAdapter(List<Article> savedArticles) {
        this.savedArticles = savedArticles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_saved_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = savedArticles.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName()); //Display the source of the article
        holder.timeTextView.setText(article.getPublishedAt());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.baseline_downloading_24) //Image will show when missing or fails to load
                .placeholder(R.drawable.baseline_downloading_24)
                .into(holder.imageView);

        holder.itemView.setOnClickListener((v -> {
            Intent intent = new Intent(v.getContext(), Reading1Activity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        }));

        boolean isBookmarked = SavedArticlesManager.isArticleBookmarked(holder.itemView.getContext(), article);
        article.setBookmarked(isBookmarked);

        if (article.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark);
        }

        // Set the click listener for the bookmark button
        holder.bookmarkButton.setOnClickListener(v -> {
            article.setBookmarked(!article.isBookmarked());
            notifyItemChanged(position);

            if (article.isBookmarked()) {
                SavedArticlesManager.addSavedArticle(v.getContext(), article);
            } else {
                SavedArticlesManager.removeSavedArticle(v.getContext(), article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedArticles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView, timeTextView;
        ImageView imageView;
        ImageButton bookmarkButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            sourceTextView = itemView.findViewById(R.id.text_source);
            imageView = itemView.findViewById(R.id.img_headline);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
            timeTextView = itemView.findViewById(R.id.text_time);
        }
    }
}