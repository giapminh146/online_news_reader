package vn.edu.usth.test;


import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_2;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_3;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_4;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_5;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_6;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_7;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_8;
import static vn.edu.usth.test.Database.DatabaseHelper.ART_COL_9;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Toast;

import vn.edu.usth.test.Database.DatabaseHelper;
import vn.edu.usth.test.Models.Article;
import vn.edu.usth.test.Models.Source;

public class SavedArticlesManager {
    private static DatabaseHelper db;


    public SavedArticlesManager(DatabaseHelper dbInstance) {
        db = dbInstance; // Initialize with the provided DatabaseHelper instance
    }


    public boolean unbookmarkArticle(String userEmail, String articleTitle) {
        return db.deleteArticle(userEmail, articleTitle);
    }

    // Add articles to saved item
    public void addSavedArticle(Context context, Article article, String userEmail) {
        if (db.saveArticle(article, userEmail)) {
            Toast.makeText(context, "Article saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error saving article.", Toast.LENGTH_SHORT).show();
        }
    }

    // Remove articles in the list of savedArticles and update SharedPreferences
    public static boolean removeSavedArticle(Context context, Article article, String userEmail) {
        boolean isRemoved = db.deleteArticle(userEmail, article.getTitle());

        if (isRemoved) {
            Toast.makeText(context, "Article removed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error removing article.", Toast.LENGTH_SHORT).show();
        }

        return isRemoved;
    }

    // Check the articles which are bookmarked or not
    public boolean isArticleBookmarked(Context context, Article article, String userEmail) {
        return db.checkUserSavedArticle(userEmail, article.getTitle());
    }

    // Retrieve saved articles from the database for the logged-in user
    public List<Article> getSavedArticles(String userEmail) {
        List<Article> articles = new ArrayList<>();
        Cursor cursor = db.getUserSavedArticles(userEmail);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String sourceName = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_2));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_3));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_4));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_5));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_6));
                String urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_7));
                String publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_8));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(ART_COL_9));

                // Create a Source object based on retrieved source ID
                Source source = new Source();
                source.setName(sourceName);

                // Create an Article object with retrieved values
                Article article = new Article(source, author, title, description, url, urlToImage, publishedAt, content);
                articles.add(article);
            }
            cursor.close();
        }

        return articles;
    }
}
