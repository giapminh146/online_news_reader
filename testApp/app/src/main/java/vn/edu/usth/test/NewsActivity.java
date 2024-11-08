package vn.edu.usth.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import vn.edu.usth.test.Database.DatabaseHelper;
import vn.edu.usth.test.Models.Article;
import vn.edu.usth.test.Models.Constants;
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
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton filterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setupSearchVew();
        setupBottomNav();
        setupTabLayout();
        setupRecyclerView();
        setupSwipeRefresh();
        setupFilterButton();

        getNews("General", null);

        // Set header fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_header, new HeaderFragment()).commit();

    }

    //Initially selected items (by default show all)
    private String selectedCountry = "All", selectedCategory = "All", selectedLanguage = "All";
    private int selectedCountryPosition = 0, selectedCategoryPosition = 0, selectedLanguagePosition = 0;

    private void filterBottomSheet() {
        View view = LayoutInflater.from(this).inflate(R.layout.filter_layout, null);
        Spinner countrySpinner = view.findViewById(R.id.countrySpinner);
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        Spinner languageSpinner = view.findViewById(R.id.languageSpinner);
        Button applyBtn = view.findViewById(R.id.applyBtn);

        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        ArrayAdapter<String> adapterCountries, adapterCategories, adapterLanguages;
        //Create an ArrayAdapter using the string array and a default spinner layout
        if (currentLanguage.equals("vi")) {
            adapterCountries = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.COUNTRIES);
            adapterCategories = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.CATEGORIES_VI);
            adapterLanguages = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.LANGUAGES);
        } else {
            adapterCountries = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.COUNTRIES);
            adapterCategories = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.CATEGORIES);
            adapterLanguages = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.LANGUAGES);
        }
        //Specify the layout to use when the list of choices appears
        adapterCountries.setDropDownViewResource(R.layout.spinner_item);
        adapterCategories.setDropDownViewResource(R.layout.spinner_item);
        adapterLanguages.setDropDownViewResource(R.layout.spinner_item);
        //Apply adapter to our spinners
        countrySpinner.setAdapter(adapterCountries);
        categorySpinner.setAdapter(adapterCategories);
        languageSpinner.setAdapter(adapterLanguages);

        //Spinner item selected listeners
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = Constants.COUNTRIES[i];
                selectedCountryPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = Constants.CATEGORIES[i];
                selectedCategoryPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = Constants.LANGUAGES[i];
                selectedLanguagePosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Setup bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        //Add layout/view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //Show bottomsheet
        bottomSheetDialog.show();

        //Apply filter on click
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dismiss dialog
                bottomSheetDialog.dismiss();
                String englishCategory = translateCategoryToEnglish(selectedCategory);
                Log.d("FILTER_TAG", "Selected Category: " + englishCategory);
                getNews(englishCategory, null);
            }
        });
    }

    // Setup the adapter for the RecycleView
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize SavedArticlesManager with DatabaseHelper instance
        DatabaseHelper dbInstance = new DatabaseHelper(this);
        SavedArticlesManager savedArticlesManager = new SavedArticlesManager(dbInstance);

        // Retrieve user email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);

        // Pass the required parameters to the adapter
        adapter = new NewsRecyclerAdapter(articleList, savedArticlesManager, userEmail);
        recyclerView.setAdapter(adapter);
    }

    private String translateCategoryToEnglish(String category) {
        if (category.equals("Tổng Hợp") || category.equals("general")) {
            return "general";
        } else if (category.equals("Kinh Doanh") || category.equals("business")) {
            return "business";
        } else if (category.equals("Giải Trí") || category.equals("entertainment")) {
            return "entertainment";
        } else if (category.equals("Sức Khỏe") || category.equals("health")) {
            return "health";
        } else if (category.equals("Khoa Học") || category.equals("science")) {
            return "science";
        } else if (category.equals("Thể Thao") || category.equals("sports")) {
            return "sports";
        } else if (category.equals("Công Nghệ") || category.equals("technology")) {
            return "technology";
        } else {
            return "general";
        }
    }

    // The method is called NewsApi
    void getNews(String Category, String query) {
        String englishCategory = translateCategoryToEnglish(Category);
        //Call the ApiKey
        NewsApiClient newsApiClient = new NewsApiClient(getResources().getString(R.string.API_KEY));
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language(selectedLanguage) //Take the news in each Language
                        .category(englishCategory) //Take the news for each Category
                        .country(selectedCountry)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged(); //Notice the adapter that the data has changed and should refresh the RecycleView
                            swipeRefreshLayout.setRefreshing(false); // Stop refresh animation after data is loaded
                            Log.d("FILTER_TAG", "Country: "+selectedCountry);
                            Log.d("FILTER_TAG", "Category: "+selectedCategory);
                            Log.d("FILTER_TAG", "Language: "+selectedLanguage);

                            if (selectedCountry.equals("All")) {
                                selectedCountry = "";
                            }
                            if (selectedCategory.equals("All")) {
                                selectedCategory = "";
                            }
                            if (selectedLanguage.equals("All")) {
                                selectedLanguage = "";
                            }
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

    private void setupSearchVew() {
        //Call search view
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("General", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupBottomNav() {
        // Call menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Reset all icons to unselected (or border version) first
            resetIcons(bottomNavigationView);

            if (itemId == R.id.navigation_home) {
                // Change icon to filled home icon
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_filled);
                return true;
            } else if (itemId == R.id.navigation_more) {
                // Change icon to filled more icon
                bottomNavigationView.getMenu().findItem(R.id.navigation_more).setIcon(R.drawable.ic_more_filled);
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.navigation_podcast) {
                // Change icon to filled podcast icon
                bottomNavigationView.getMenu().findItem(R.id.navigation_podcast).setIcon(R.drawable.ic_podcasts);
                startActivity(new Intent(getApplicationContext(), PodcastActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });
    }

    private void resetIcons(BottomNavigationView bottomNavigationView) {
        // Reset all icons to unselected (border version)
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_border);
        bottomNavigationView.getMenu().findItem(R.id.navigation_more).setIcon(R.drawable.ic_more_border);
        bottomNavigationView.getMenu().findItem(R.id.navigation_podcast).setIcon(R.drawable.ic_podcast_border);
    }


    private void setupTabLayout() {
        TabLayout tabLayout = findViewById(R.id.TabLayoutsMenu);

        //Set addOnTabSelectedListener to tabLayout: each tab item opens corresponding category
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CharSequence tabText = tab.getText();
                String tabTextString = tabText != null ? tabText.toString() : "";//Check if tab Item text is null, if null return ""
                String englishCategory = translateCategoryToEnglish(tabTextString);
                getNews(englishCategory, null); // Call with the English category name
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupFilterButton() {
        //Click to show filter dialog
        filterBtn = findViewById(R.id.filter_button);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBottomSheet();
            }
        });
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
            String tabTextString = tabText != null ? tabText.toString() : "";
            String englishCategory = translateCategoryToEnglish(tabTextString);
            getNews(englishCategory, null); // Call with the English category name
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