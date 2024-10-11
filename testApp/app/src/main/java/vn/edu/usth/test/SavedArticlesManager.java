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

    public static void addSavedArticle(Context context, Article article) {
        savedArticles.add(article);
        saveToPreferences(context, article, true);
    }


    public static void removeSavedArticle(Context context, Article article) {
        savedArticles.remove(article);
        saveToPreferences(context, article, false);
    }

    private static void saveToPreferences(Context context, Article article, boolean isBookmarked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BOOKMARK_KEY_PREFIX + article.getTitle(), isBookmarked); // Assuming Article has an ID
        editor.apply();
    }

    public static boolean isArticleBookmarked(Context context, Article article) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(BOOKMARK_KEY_PREFIX + article.getTitle(), false);
    }
    public static List<Article> getSavedArticles() {
        return new ArrayList<>(savedArticles);
    }
}
