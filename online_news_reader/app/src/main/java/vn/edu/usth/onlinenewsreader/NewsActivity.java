package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (savedInstanceState == null) {
            // Load MainScreenFragment into the container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_screen, new MainScreenFragment())
                    .commit();

            // Load MenuFragment into the container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_menu, new MenuFragment())
                    .commit();
        }
    }
}

