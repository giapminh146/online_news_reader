package vn.edu.usth.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import vn.edu.usth.test.Models.Article;
import vn.edu.usth.test.Models.request.TopHeadlinesRequest;
import vn.edu.usth.test.Models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    //A RecyclerView is used to display the list of articles
    RecyclerView recyclerView;
    //This is created to store the news articles
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;

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
                String tabTextString = tabText !=null ? tabText.toString() :"";//Check if tab Item text is null, if null return ""
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
                        runOnUiThread(()-> {
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged(); //Notice the adapter that the data has changed and should refresh the RecycleView
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Got failure", "Error: ",throwable);
                    }
                }
        );
    }
}
