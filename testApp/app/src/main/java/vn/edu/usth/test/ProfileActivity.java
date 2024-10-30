package vn.edu.usth.test;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vn.edu.usth.test.Database.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {
    private TextView accountNameTextView;
    private TextView accountEmailTextView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DatabaseHelper(this);

        // Retrieve saved user info
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        String userEmail = sharedPreferences.getString("userEmail", "");

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Delete the title in the bar
            actionBar.setDisplayShowTitleEnabled(false);
            // Create the back button and return the page before
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            drawable.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        accountNameTextView = findViewById(R.id.boxed_account_name);
        accountEmailTextView = findViewById(R.id.boxed_account_email);
        accountEmailTextView.setText(userEmail);
        accountNameTextView.setText(userName);
        Button editProfileButton = findViewById(R.id.edit_profile_button);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
    }

    private void showEditProfileDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);

        EditText editName = dialog.findViewById(R.id.edit_name);
        EditText editEmail = dialog.findViewById(R.id.edit_email);
        Button saveButton = dialog.findViewById(R.id.save_button);

        // Hiển thị tên và email hiện tại
        editName.setText(accountNameTextView.getText().toString());
        editEmail.setText(accountEmailTextView.getText().toString());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString();
                String newEmail = editEmail.getText().toString();
                String oldEmail = accountEmailTextView.getText().toString(); // Original email

                // Cập nhật TextView với thông tin mới
                accountNameTextView.setText(newName);
                accountEmailTextView.setText(newEmail);

                // Update in the database
                if( db.updateUser(oldEmail, newName, newEmail)){
                    SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", newName);
                    editor.putString("userEmail", newEmail);
                    editor.apply();

                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss(); // Đóng dialog
            }
        });

        dialog.show(); // Hiện dialog
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