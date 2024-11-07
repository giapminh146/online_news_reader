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
    private SavedArticlesManager savedArticlesManager;
    private String userEmail; // Store the user's email for article management
    private OnArticleUnbookmarkedListener onArticleUnbookmarkedListener;

    public SavedArticlesAdapter(List<Article> savedArticles, SavedArticlesManager manager, String userEmail) {
        this.savedArticles = savedArticles;
        this.savedArticlesManager = manager;
        this.userEmail = userEmail;
    }

    public interface OnArticleUnbookmarkedListener {
        void onArticleUnbookmarked(int position);
    }

    public void setOnArticleUnbookmarkedListener(OnArticleUnbookmarkedListener listener) {
        this.onArticleUnbookmarkedListener = listener;
    }

    // Unbookmarking logic
    private void unbookmarkArticle(int position) {
        Article article = savedArticles.get(position);
        boolean isRemoved = savedArticlesManager.unbookmarkArticle(userEmail, article.getTitle());

        if (isRemoved) {
            savedArticles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, savedArticles.size());

            // Trigger the listener if it's set
            if (onArticleUnbookmarkedListener != null) {
                onArticleUnbookmarkedListener.onArticleUnbookmarked(position);
            }
        }
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
        holder.sourceTextView.setText(article.getSource().getName()); // Display the source of the article
        holder.timeTextView.setText(article.getPublishedAt());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.baseline_downloading_24) // Image will show when missing or fails to load
                .placeholder(R.drawable.baseline_downloading_24)
                .into(holder.imageView);

        holder.itemView.setOnClickListener((v -> {
            Intent intent = new Intent(v.getContext(), Reading1Activity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        }));

        boolean isBookmarked = savedArticlesManager.isArticleBookmarked(holder.itemView.getContext(), article, userEmail);
        article.setBookmarked(isBookmarked);

        if (article.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark);
        }

        // Set the click listener for the bookmark button
//        holder.bookmarkButton.setOnClickListener(v -> {
//            article.setBookmarked(!article.isBookmarked());
//            notifyItemChanged(position);
//
//            if (article.isBookmarked()) {
//                savedArticlesManager.addSavedArticle(v.getContext(), article, userEmail);
//            } else {
//                savedArticlesManager.removeSavedArticle(v.getContext(), article, userEmail); // Use the manager instance
//            }
//        });
        // Set the click listener for the bookmark button
        holder.bookmarkButton.setOnClickListener(v -> {
            // Get the current adapter position of the article
            int currentPosition = holder.getAdapterPosition();

            // Ensure the position is valid before proceeding
            if (currentPosition != RecyclerView.NO_POSITION) {
                article.setBookmarked(!article.isBookmarked());
                notifyItemChanged(currentPosition);

                if (article.isBookmarked()) {
                    savedArticlesManager.addSavedArticle(v.getContext(), article, userEmail);
                    holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
                } else {
                    boolean isRemoved = savedArticlesManager.removeSavedArticle(v.getContext(), article, userEmail);

                    if (isRemoved) {
                        // Update icon and remove the item from the list
                        holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark);
                        savedArticles.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, savedArticles.size());
                    }
                }
            }
        });

    }

    // Method to remove an article from the list and notify the adapter
    public void removeArticle(Article article) {
        int position = savedArticles.indexOf(article);
        if (position != -1) {
            savedArticles.remove(position);
            notifyItemRemoved(position);
        }
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
