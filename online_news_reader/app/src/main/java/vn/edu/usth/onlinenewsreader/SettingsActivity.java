package vn.edu.usth.onlinenewsreader;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Change color of Language Spinner
        Spinner languageSpinner = findViewById(R.id.language_select);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, R.layout.spinner_item); // Use the custom layout

        adapter.setDropDownViewResource(R.layout.spinner_item); // Set for the dropdown view
        languageSpinner.setAdapter(adapter);
        languageSpinner.setPopupBackgroundResource(R.color.white);


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
        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // Delete the title in the bar
        actionBar.setDisplayShowTitleEnabled(false);
        // Create the back button and return the page before
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Click on account setting to login (account) activity
        LinearLayout account_setting = findViewById(R.id.account_setting_layout);

        account_setting.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // In actionbar, this is default home, not any id. It presents for "back" or "up" button
        if (item.getItemId() == android.R.id.home) {
            // Turn back the previous page
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}