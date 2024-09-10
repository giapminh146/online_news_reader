package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        MainScreenFragment mainScreenFragment = new MainScreenFragment();

        MoreFragment moreFragment = new MoreFragment();

        // Loading the main screen fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_screen, mainScreenFragment).commit();

        // Later, when "More" is clicked, you can replace it with the More fragment:
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_more, moreFragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}











