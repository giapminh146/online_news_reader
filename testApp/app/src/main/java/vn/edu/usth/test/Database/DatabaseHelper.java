package vn.edu.usth.test.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import vn.edu.usth.test.Models.Article;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME  = "online_news_db";
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_ARTICLES = "articles";

    // Users table columns
    public static final String COL_1= "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";

    // Articles table columns
    public static final String ART_COL_1 = "ID";
    public static final String ART_COL_2 = "SOURCE_NAME";
    public static final String ART_COL_3 = "AUTHOR";
    public static final String ART_COL_4 = "TITLE";
    public static final String ART_COL_5 = "DESCRIPTION";
    public static final String ART_COL_6 = "URL";
    public static final String ART_COL_7 = "URL_TO_IMAGE";
    public static final String ART_COL_8 = "PUBLISHED_AT";
    public static final String ART_COL_9 = "CONTENT";
    public static final String ART_COL_10 = "USER_EMAIL";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT, PASSWORD TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_NAME_ARTICLES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SOURCE_NAME TEXT, AUTHOR TEXT, TITLE TEXT, DESCRIPTION TEXT, URL TEXT, URL_TO_IMAGE TEXT, PUBLISHED_AT TEXT, CONTENT TEXT, USER_EMAIL TEXT, FOREIGN KEY (USER_EMAIL) REFERENCES " + TABLE_NAME_USERS + "(EMAIL))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_ARTICLES);
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, password);
        long result =  db.insert(TABLE_NAME_USERS, null, contentValues);
        return result != -1; // return false if is not inserted
    }

    // Cursor object helps for data set traversal
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password}); // Cursor object helps for data set traversal
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Method to get Username according to email
    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        Cursor cursor = db.rawQuery("SELECT NAME FROM " + TABLE_NAME_USERS + " WHERE EMAIL=?", new String[]{email});

        if (cursor.moveToFirst()) {  // Move to the first result if available
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_2));
        }
        cursor.close();
        return name;
    }

    // Method to update user information
    public boolean updateUser(String oldEmail, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, newName);
        contentValues.put(COL_3, newEmail);

        int result = db.update(TABLE_NAME_USERS, contentValues, "EMAIL=?", new String[]{oldEmail});
        return result > 0;
    }

    // Method to save an article
    public boolean saveArticle(Article article, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ART_COL_2, article.getSource().getName());
        contentValues.put(ART_COL_3, article.getAuthor());
        contentValues.put(ART_COL_4, article.getTitle());
        contentValues.put(ART_COL_5, article.getDescription());
        contentValues.put(ART_COL_6, article.getUrl());
        contentValues.put(ART_COL_7, article.getUrlToImage());
        contentValues.put(ART_COL_8, article.getPublishedAt());
        contentValues.put(ART_COL_9, article.getContent());
        contentValues.put(ART_COL_10, userEmail);
        long result = db.insert(TABLE_NAME_ARTICLES, null, contentValues);
        return result != -1; // If not inserted, return false
    }

    // Method to delete an article for a user
    public boolean deleteArticle(String userEmail, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME_ARTICLES, "USER_EMAIL=? AND TITLE=?", new String[]{userEmail, title});
        return result > 0;
    }

    // Method to check if an article is saved for a specific user
    public boolean checkUserSavedArticle(String userEmail, String title) {
        if (userEmail == null || title == null) {
            Log.e("DatabaseHelper", "User email or title is null");
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME_ARTICLES, null, "USER_EMAIL=? AND TITLE=?",
                    new String[]{userEmail, title}, null, null, null);
            return (cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Method to retrieve saved articles for an user
    public Cursor getUserSavedArticles(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME_ARTICLES, null, "USER_EMAIL = ?", new String[]{userEmail}, null, null, null);
    }

    // Method to delete articles for a user when logout
    public void deleteUserArticles(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ARTICLES, "USER_EMAIL = ?", new String[]{userEmail});
        db.close();
    }

    // Method to check if an email is already used
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE EMAIL=?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}

