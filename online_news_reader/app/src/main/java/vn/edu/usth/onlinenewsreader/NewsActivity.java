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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // Setup ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(5);
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(newsPagerAdapter);

        // Setup TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, new HeaderFragment())
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu, new MenuFragment()).commit();

        // Set up OnClick Listener
        LinearLayout homeLayout = findViewById(R.id.homeLayout);
        LinearLayout moreLayout = findViewById(R.id.moreLayout);
        TextView moreTextView = findViewById(R.id.moreTextView);
        TextView homeTextView = findViewById(R.id.homeTextView);
        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton moreButton = findViewById(R.id.moreButton);

        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.setVisibility(View.GONE);

                // Create a new FragmentTransaction for the MoreFragment
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.view_pager_container, new MoreFragment());
                fragmentTransaction.addToBackStack("home");
                fragmentTransaction.commit();
            }
        });

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.setVisibility(View.VISIBLE);

                // Create a new FragmentTransaction to replace MoreFragment with the ViewPager
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                // hide more fragment
                Fragment moreFragment = getSupportFragmentManager().findFragmentById(R.id.view_pager_container);
                if (moreFragment != null) {
                    fragmentTransaction.hide(moreFragment);
                }
                // add to back stack
                fragmentTransaction.addToBackStack("more");
                fragmentTransaction.commit();
            }
        });
        // Handle back navigation
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Check if there's a fragment in the back stack
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Get the top back stack entry
                    String topFragmentName = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();

                    if ("home".equals(topFragmentName)) {
                        // Pop the stack and show TabLayout
                        fragmentManager.popBackStack();
                        tabLayout.setVisibility(View.VISIBLE);
                    } else if ("more".equals(topFragmentName)) {
                        // Pop the stack and hide TabLayout
                        fragmentManager.popBackStack();
                        tabLayout.setVisibility(View.GONE);
                    }
                } else {
                    // Exit the activity if no fragments left in back stack
                    finish();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

    }
}

