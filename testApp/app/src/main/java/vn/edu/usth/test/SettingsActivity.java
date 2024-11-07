package vn.edu.usth.test;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final String[] languages = {"Select","English", "Tiếng Việt"};
    private CheckBox checkBoxDarkMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved language from SharedPreferences
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("selected_language", "en"); // default to English
        setLocale(lang);

        setContentView(R.layout.activity_settings);

        checkBoxDarkMode = findViewById(R.id.checkbox_dark_mode);
        // Load dark mode before setting content view
        boolean isDarkMode = sharedPreferences.getBoolean("night", false);

        if(isDarkMode){
            checkBoxDarkMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }

        // A listener for the switch
        checkBoxDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDarkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",true);
                }
                editor.apply();
            }
        });

        // Set up the adapter for the spinner
        Spinner languageSpinner = findViewById(R.id.language_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set up the listener for the spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: // English
                        changeLanguage("en");
                        break;
                    case 2: // Vietnamese
                        changeLanguage("vi");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.arrow_back);
            Drawable drawable = toolbar.getNavigationIcon();
            if (drawable != null) {
                drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }

        // Click on account setting to navigate to the profile activity if logged in, else to login screen
        LinearLayout accountSettingLayout = findViewById(R.id.account_setting_layout);
        accountSettingLayout.setOnClickListener(view -> {
            SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
            boolean isLoggedIn = userPrefs.contains("userEmail");
            if(isLoggedIn) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            } else {
                // When the user is not logged in, show message and go to login screen
                Toast.makeText(this, "Please log in to access Account Settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout logoutButton = findViewById(R.id.logout_section);
        logoutButton.setOnClickListener(v -> logout());

        // Check if the user is logged out and disable the logout button
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        if (!userPrefs.contains("userEmail")) {
            logoutButton.setEnabled(false);
        } else {
            logoutButton.setEnabled(true);
            logoutButton.setOnClickListener(v -> logout());
        }
    }

    // Method to change language and restart NewsActivity
    private void changeLanguage(String lang) {
        // Save the selected language to SharedPreferences
        editor = sharedPreferences.edit();
        editor.putString("selected_language", lang);
        editor.apply();

        setLocale(lang);
        Intent intent = new Intent(SettingsActivity.this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Set the language
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);  // Create a new Locale object with the language code provided
        Resources res = getResources(); // Get the Resources object for the app
        DisplayMetrics dm = res.getDisplayMetrics(); // Get the display metrics of the device
        Configuration conf = res.getConfiguration(); // Get the current configuration object
        conf.setLocale(myLocale);  // Set the new language for the configuration
        res.updateConfiguration(conf, dm); // Update the app's resources configuration with the new settings and display metrics
    }

    // Method to perform the Log Out function
    private void logout(){
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPrefs.edit();
        userEditor.clear();
        userEditor.apply();

        // Redirecting to login screen
        Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack to avoid going back
        startActivity(intent);

        // Show logout confirmation
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
