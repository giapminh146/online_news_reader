package vn.edu.usth.onlinenewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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

    private ImageButton homeButton, moreButton;
    private TextView homeTextView, moreTextView;
    private LinearLayout homeLayout, moreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Setup ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(5);
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(newsPagerAdapter);

        // Setup TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Replace fragments
        fragmentManager.beginTransaction().replace(R.id.fragment_header, new HeaderFragment()).commit();
        fragmentManager.beginTransaction().replace(R.id.menu, new MenuFragment()).commit();

        // Initialize menu components
        homeLayout = findViewById(R.id.homeLayout);
        moreLayout = findViewById(R.id.moreLayout);
        homeTextView = findViewById(R.id.homeTextView);
        moreTextView = findViewById(R.id.moreTextView);
        homeButton = findViewById(R.id.homeButton);
        moreButton = findViewById(R.id.moreButton);

        // Initially set the Home icon to filled and the text color to red
        homeButton.setImageResource(R.drawable.ic_home_filled);
        homeTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

        // Set up OnClickListener for More fragment
        View.OnClickListener moreClickListener = view -> {
            // Change More icon to filled version and text color to red
            moreButton.setImageResource(R.drawable.ic_more_filled);
            moreTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

            // Reset Home icon and text color
            homeButton.setImageResource(R.drawable.ic_home_border);
            homeTextView.setTextColor(ContextCompat.getColor(this, R.color.black));

            tabLayout.setVisibility(View.GONE);

            // Switch to MoreFragment
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.view_pager_container, new MoreFragment());
            ft.addToBackStack("home");
            ft.commit();
        };

        moreLayout.setOnClickListener(moreClickListener);
        moreTextView.setOnClickListener(moreClickListener);
        moreButton.setOnClickListener(moreClickListener);

        // Set up OnClickListener for Home section
        View.OnClickListener homeClickListener = view -> {
            // Change Home icon to filled version and text color to custom_blue
            homeButton.setImageResource(R.drawable.ic_home_filled);  // Use your filled home icon
            homeTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

            // Reset More icon and text color
            moreButton.setImageResource(R.drawable.ic_more_border);  // Use your outlined more icon
            moreTextView.setTextColor(ContextCompat.getColor(this, R.color.black));

            tabLayout.setVisibility(View.VISIBLE);

            // Switch back to MainScreenFragment (via ViewPager)
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment moreFragment = fragmentManager.findFragmentById(R.id.view_pager_container);
            if (moreFragment != null) {
                ft.hide(moreFragment);
            }
            ft.addToBackStack("more");
            ft.commit();
        };

        homeLayout.setOnClickListener(homeClickListener);
        homeTextView.setOnClickListener(homeClickListener);
        homeButton.setOnClickListener(homeClickListener);

        // Handle back navigation
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    String topFragmentName = fragmentManager.getBackStackEntryAt(
                            fragmentManager.getBackStackEntryCount() - 1).getName();
                    // Handle back to home
                    if ("home".equals(topFragmentName)) {
                        fragmentManager.popBackStack();
                        tabLayout.setVisibility(View.VISIBLE);
                        homeButton.setImageResource(R.drawable.ic_home_filled);
                        homeTextView.setTextColor(ContextCompat.getColor(NewsActivity.this, R.color.red));
                        moreButton.setImageResource(R.drawable.ic_more_border);
                        moreTextView.setTextColor(ContextCompat.getColor(NewsActivity.this, R.color.black));
                    }
                    // Handle back to more
                    else if ("more".equals(topFragmentName)) {
                        fragmentManager.popBackStack();
                        tabLayout.setVisibility(View.GONE);
                        moreButton.setImageResource(R.drawable.ic_more_filled);
                        moreTextView.setTextColor(ContextCompat.getColor(NewsActivity.this, R.color.red));
                        homeButton.setImageResource(R.drawable.ic_home_border);
                        homeTextView.setTextColor(ContextCompat.getColor(NewsActivity.this, R.color.black));
                    }
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}


