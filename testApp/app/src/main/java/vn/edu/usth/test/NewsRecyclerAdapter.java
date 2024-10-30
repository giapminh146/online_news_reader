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
    private List<Article> articleList;

    public NewsRecyclerAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Call the frame that will show in the main screen
        View view;
        if (viewType == 0) {
            // Inflate layout cho bài báo đầu tiên
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        } else {
            // Inflate layout cho các bài báo tiếp theo
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_saved_row, parent, false);
        }
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle()); //Sets the article's title in the TextView
        holder.sourceTextView.setText(article.getSource().getName()); //Display the source of the article
        if (holder.descriptionTextView != null) {
            holder.descriptionTextView.setText(article.getDescription());
        }
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

        // Check status bookmark from SharedPreferences
        boolean isBookmarked = SavedArticlesManager.isArticleBookmarked(holder.itemView.getContext(), article);
        // Call isArticleBookmarked() from SavedArticlesManager to check from SharedPreferences
        // Set that answer with isBookmarked, check and set icon bookmark
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
            // Toggle bookmark state
            if (article.isBookmarked()) {
                SavedArticlesManager.addSavedArticle(v.getContext(), article);
            } else {
                SavedArticlesManager.removeSavedArticle(v.getContext(), article);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    //Update articleList in RecycleView
    void updateData(List<Article> data) {
        articleList.clear(); // Clear all of the articles have in articleList
        // Filter out articles with titles containing "Removed"
        for (Article article : data) {
            if (!article.getTitle().contains("Removed")) {
                articleList.add(article); // Add only articles that do not contain "Removed" in title
            }
        }
        notifyDataSetChanged(); // Update recycle
    }

    @Override
    public int getItemCount() {
        return articleList.size(); //Tells the adapter how many items to display in RecycleView
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView, descriptionTextView, timeTextView;
        ImageView imageView;
        ImageButton bookmarkButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            sourceTextView = itemView.findViewById(R.id.text_source);
            imageView = itemView.findViewById(R.id.img_headline);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            timeTextView = itemView.findViewById(R.id.text_time);
        }
    }
}