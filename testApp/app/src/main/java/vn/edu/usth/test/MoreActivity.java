package vn.edu.usth.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.usth.test.Models.Article;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vn.edu.usth.test.Database.DatabaseHelper;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        // Retrieve user email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);

        // Check if userEmail is null, and redirect to login if necessary
        if (userEmail == null) {
            Toast.makeText(this, "Please log in to access saved articles.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish(); // Close MoreActivity so the user cannot return without logging in
            return;
        }

        //Call the header
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, new HeaderFragment()).commit();

        // Initialize SavedArticlesManager with DatabaseHelper instance
        DatabaseHelper db= new DatabaseHelper(this);
        SavedArticlesManager savedArticlesManager = new SavedArticlesManager(db);

        // Retrieve saved articles using the savedArticlesManager instance
        List<Article> savedArticles = savedArticlesManager.getSavedArticles(userEmail);

        RecyclerView savedArticlesRecyclerView = findViewById(R.id.recycleViewId);
        SavedArticlesAdapter savedArticlesAdapter = new SavedArticlesAdapter(savedArticles, savedArticlesManager, userEmail);
        savedArticlesAdapter.setOnArticleUnbookmarkedListener(position -> {
            // Remove the article from the list
            savedArticles.remove(position);
            // Notify the adapter of the change
            savedArticlesAdapter.notifyItemRemoved(position);
            savedArticlesAdapter.notifyItemRangeChanged(position, savedArticles.size());
        });
        savedArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedArticlesRecyclerView.setAdapter(savedArticlesAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_more);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_more) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.navigation_podcast) {
                startActivity(new Intent(getApplicationContext(), PodcastActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            return false;
        });

        setAccountButtonClickListener(R.id.accountButton);
        setAccountButtonClickListener(R.id.accountText);
        setOnClickListener(R.id.settingsButton, SettingsActivity.class);
        setOnClickListener(R.id.settingsText, SettingsActivity.class);
    }

    // Helper method to set OnClickListener specifically for Account button
    private void setAccountButtonClickListener(int viewId){
        findViewById(viewId).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
            String userName = sharedPreferences.getString("userName", null);

            // If user is logged in, open ProfileActivity; else, open AccountActivity
            Class<?> activityToOpen = (userName != null) ? ProfileActivity.class : AccountActivity.class;

            Intent intent = new Intent(MoreActivity.this, activityToOpen);
            startActivity(intent);
        });
    }

    // Helper method to set OnClickListener
    private void setOnClickListener(int viewId, Class<?> targetActivity) {
        findViewById(viewId).setOnClickListener(v -> {
            Intent intent = new Intent(MoreActivity.this, targetActivity);
            startActivity(intent);
        });
    }
}