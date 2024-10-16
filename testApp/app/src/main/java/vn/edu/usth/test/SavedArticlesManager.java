package vn.edu.usth.test;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.test.Models.Article;

public class SavedArticlesManager {
    private static List<Article> savedArticles = new ArrayList<>();

    public static void addSavedArticle(Article article) {
        savedArticles.add(article);
    }

    public static void removeSavedArticle(Article article) {
        savedArticles.remove(article);
    }

    public static List<Article> getSavedArticles() {
        return new ArrayList<>(savedArticles);
    }
}
