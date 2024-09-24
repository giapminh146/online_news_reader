package vn.edu.usth.test;

public class NewsItem {
    private String title;
    private String description;
    private String imageUrl;
    private String articleUrl;

    public NewsItem(String title, String description, String imageUrl, String articleUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }
}

