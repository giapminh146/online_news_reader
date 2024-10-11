package vn.edu.usth.test;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;

import vn.edu.usth.test.Models.Article;

public class SavedArticlesManager {
    private static List<Article> savedArticles = new ArrayList<>();
    private static final String PREF_NAME = "SavedArticlesPref";
    private static final String BOOKMARK_KEY_PREFIX = "BOOKMARK_";

    // Add articles to saved item and set status into SharedPreferences
    public static void addSavedArticle(Context context, Article article) {
        for (Article savedArticle : savedArticles) {
            if (savedArticle.getTitle().equals(article.getTitle())) {
                return; // If the article already exists, do not add it again
            }
        }
        savedArticles.add(article);
        saveToPreferences(context, article, true);
    }

    // Remove articles in the list of savedArticles and update SharedPreferences
    public static void removeSavedArticle(Context context, Article article) {
        savedArticles.removeIf(savedArticle -> savedArticle.getTitle().equals(article.getTitle())); // Browse each article (saved Article) in the saved Articles list.
        //If the title of savedArticle matches the title of the article you want to delete, this article will be removed from the savedArticles list.
        saveToPreferences(context, article, false);
    }

    // Remember the status of bookmark into SharedPreferences
    private static void saveToPreferences(Context context, Article article, boolean isBookmarked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String bookmarkKey = BOOKMARK_KEY_PREFIX + article.getTitle();
        if (isBookmarked && !sharedPreferences.contains(bookmarkKey)) {
            editor.putBoolean(bookmarkKey, true);
        } else if (!isBookmarked) {
            editor.remove(bookmarkKey);
        } //Saved the article with name BOOKMARK_KEY_PREFIX+title
        editor.apply();
    }

    // Check the articles which is bookmarked or not
    public static boolean isArticleBookmarked(Context context, Article article) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Call BOOKMARK_KEY_PREFIX+title again and check bookmark or not
        return sharedPreferences.getBoolean(BOOKMARK_KEY_PREFIX + article.getTitle(), false);
    }

    // Display the list of articles on Saved Item
    public static List<Article> getSavedArticles() {
        return new ArrayList<>(savedArticles);
    }
}
