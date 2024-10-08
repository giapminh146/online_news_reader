package vn.edu.usth.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import vn.edu.usth.test.Models.Article;
import vn.edu.usth.test.Models.request.TopHeadlinesRequest;
import vn.edu.usth.test.Models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //A RecyclerView is used to display the list of articles
    RecyclerView recyclerView;
    //This is created to store the news articles
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        TabLayout tabLayout = findViewById(R.id.TabLayoutsMenu);

        //Set addOnTabSelectedListener to tabLayout: each tab item opens corresponding category
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence tabText = tab.getText();
                String tabTextString = tabText != null ? tabText.toString() : "";//Check if tab Item text is null, if null return ""
                getNews(tabTextString);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //Call the recycleView id
        recyclerView = findViewById(R.id.recycleViewId);
        setupRecyclerView();
        getNews("General");

        //Call the header
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_header, new HeaderFragment()).commit();

        //Call swipe to refresh
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    // Setup the adapter for the RecycleView
    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    // The method is called NewsApi
    void getNews(String Category) {
        //Call the ApiKey
        NewsApiClient newsApiClient = new NewsApiClient("3ebfc3d8ab8945cc92c261a170d8ac96");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en") //Take the news in English
                        .category(Category) //Take the news for each Category
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged(); //Notice the adapter that the data has changed and should refresh the RecycleView
                            swipeRefreshLayout.setRefreshing(false); // Stop refresh animation after data is loaded
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Got failure", "Error: ", throwable);
                        swipeRefreshLayout.setRefreshing(false); // Stop refresh animation after data is loaded
                    }
                }
        );
    }

    //The method to refresh and get new news articles
    @Override
    public void onRefresh() {
        articleList.clear(); // Clear the current list to avoid duplication
        adapter.notifyDataSetChanged();

        TabLayout tabLayout = findViewById(R.id.TabLayoutsMenu);
        TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        if (selectedTab != null) {
            CharSequence tabText = selectedTab.getText();
            String tabTextString = tabText != null ? tabText.toString() : "General";
            getNews(tabTextString); // Fetch new articles for the selected category
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, "News refreshed", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }
}
