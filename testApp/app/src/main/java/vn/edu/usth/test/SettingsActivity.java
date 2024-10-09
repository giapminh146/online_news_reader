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
                if (position == 1) {
                    // Change to English
                    setLocale("en");
                } else if (position == 2) {
                    // Change to Vietnamese
                    setLocale("vi");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SwitchCompat mySwitch = findViewById(R.id.switch_notifications);
        // Set the default color to red
        mySwitch.getThumbDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.light_red), PorterDuff.Mode.SRC_IN);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mySwitch.getThumbDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.blue), PorterDuff.Mode.SRC_IN);
                } else {
                    mySwitch.getThumbDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.light_red), PorterDuff.Mode.SRC_IN);
                }
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

        // Click on account setting to navigate to the profile activity
        LinearLayout accountSettingLayout = findViewById(R.id.account_setting_layout);
        accountSettingLayout.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
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
