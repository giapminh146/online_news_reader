package vn.edu.usth.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //Call the header
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_header, new HeaderFragment()).commit();

        RecyclerView savedArticlesRecyclerView = findViewById(R.id.recycleViewId);
        SavedArticlesAdapter savedArticlesAdapter = new SavedArticlesAdapter(SavedArticlesManager.getSavedArticles());
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

        setOnClickListener(R.id.accountButton, AccountActivity.class);
        setOnClickListener(R.id.accountText, AccountActivity.class);
        setOnClickListener(R.id.settingsButton, SettingsActivity.class);
        setOnClickListener(R.id.settingsText, SettingsActivity.class);
    }

    // Helper method to set OnClickListener
    private void setOnClickListener(int viewId, Class<?> targetActivity) {
        findViewById(viewId).setOnClickListener(v -> {
            Intent intent = new Intent(MoreActivity.this, targetActivity);
            startActivity(intent);
        });
    }
}