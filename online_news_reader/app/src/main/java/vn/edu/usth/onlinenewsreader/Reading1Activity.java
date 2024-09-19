package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class Reading1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_header, new HeaderFragment()).commit();

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Delete the title in the bar
            actionBar.setDisplayShowTitleEnabled(false);
            // Create the back button and return the page before
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Set up more which have "share" button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private boolean isBookmarked = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        // A notification is appeared when it is clicked
        if (itemId == R.id.bookMark) {
            if (isBookmarked) {
                // Change to the unbookmarked state
                item.setIcon(R.drawable.ic_bookmark_white);
                Toast.makeText(this, "Unbookmarked", Toast.LENGTH_SHORT).show();
                isBookmarked = false;
            } else {
                // Change to the bookmarked state
                item.setIcon(R.drawable.ic_bookmark_filled);
                Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show();
                isBookmarked = true;
            }
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            // Turn back the previous page
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}