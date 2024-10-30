package vn.edu.usth.test.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME  = "online_news_db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1= "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, password);
        long result =  db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // return false if is not inserted
    }

    // Cursor object helps for data set traversal
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password}); // Cursor object helps for data set traversal
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Method to get Username according to email
    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        Cursor cursor = db.rawQuery("SELECT NAME FROM " + TABLE_NAME + " WHERE EMAIL=?", new String[]{email});

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

        int result = db.update(TABLE_NAME, contentValues, "EMAIL=?", new String[]{oldEmail});
        return result > 0;
    }
}

