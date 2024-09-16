package vn.edu.usth.onlinenewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // Create object to enable the Listener on
        TextView register = findViewById(R.id.txtRegister);

        // Click the Listener for register Text
        register.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }
}