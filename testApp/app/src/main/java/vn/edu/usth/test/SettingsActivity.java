package vn.edu.usth.test;


import android.content.Intent;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final String[] languages = {"Select","English", "Tiếng Việt"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Spinner languageSpinner = findViewById(R.id.language_select);

        // Set up the adapter for the spinner
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

        SwitchCompat mySwitch = findViewById(R.id.switch_notifications);
        // Set the default color to red
        applySwitchColor(mySwitch, false); // Set initial color
        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> applySwitchColor(mySwitch, isChecked));

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

        // Click on account setting to navigate to the profile activity
        LinearLayout accountSettingLayout = findViewById(R.id.account_setting_layout);
        accountSettingLayout.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }

    // Method to change language and restart NewsActivity
    private void changeLanguage(String lang) {
        setLocale(lang);
        Intent intent = new Intent(SettingsActivity.this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);  // Create a new Locale object with the language code provided
        Resources res = getResources(); // Get the Resources object for the app
        DisplayMetrics dm = res.getDisplayMetrics(); // Get the display metrics of the device
        Configuration conf = res.getConfiguration(); // Get the current configuration object
        conf.setLocale(myLocale);  // Set the new language for the configuration
        res.updateConfiguration(conf, dm); // Update the app's resources configuration with the new settings and display metrics
    }

    // Method to apply color based on switch state
    private void applySwitchColor(SwitchCompat switchCompat, boolean isChecked) {
        int color = isChecked ? R.color.blue : R.color.light_red;
        switchCompat.getThumbDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, color), PorterDuff.Mode.SRC_IN);
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
