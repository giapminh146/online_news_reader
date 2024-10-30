package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Delete the title in the bar
            actionBar.setDisplayShowTitleEnabled(false);
            // Create the back button and return the page before
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // In actionbar, this is default home, not any id. It presents for "back" or "up" button
        if (item.getItemId() == android.R.id.home) {
            // Turn back the previous page
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}