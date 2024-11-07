package vn.edu.usth.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.util.Log;

import vn.edu.usth.test.Database.DatabaseHelper;

public class AccountActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText loginEmail, loginPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize db helper
        db = new DatabaseHelper(this);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        buttonLogin = findViewById(R.id.buttonLogIn);

         //Set up login button logic
        buttonLogin.setOnClickListener(view -> loginUser());

        // Create object to enable the Listener on
        TextView register = findViewById(R.id.txtRegister);

        // Click the Listener for register Text
        register.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // Delete the title in the bar
        actionBar.setDisplayShowTitleEnabled(false);
        // Create the back button and return the page before
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            drawable.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void loginUser(){
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean isAuthenticated = db.checkUser(email, password);
            if(isAuthenticated) {
                SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userEmail", email);
                editor.putString("userName", db.getUserName(email));
                editor.apply();
                Log.i("Checking info", "email retrieved: " +email);
                Log.i("Checking info", "name retrieved: " +db.getUserName(email));
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // Navigate to NewsActivity
                startActivity(new Intent(AccountActivity.this, NewsActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Using intent extra to smoother the behaviour
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Directly navigate back to NewsActivity
            Intent intent = new Intent(AccountActivity.this, NewsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
