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
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (savedInstanceState == null) {

            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setOffscreenPageLimit(3);

            NewsPagerAdapter weatherPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(weatherPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);

//            // Load MainScreenFragment into the container
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_main_screen, new MainScreenFragment())
//                    .commit();

            getSupportFragmentManager().beginTransaction()
                            .replace(R.id.header_fragment, new HeaderFragment())
                                    .commit();

            // Load MenuFragment into the container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_menu, new MenuFragment())
                    .commit();
        }
    }
}

