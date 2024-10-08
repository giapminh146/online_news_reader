package vn.edu.usth.onlinenewsreader;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.activity.OnBackPressedCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class MoreFragment extends Fragment {
    private final Map<ImageButton, Boolean> bookmarkStates = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_fragment, container, false);

        ImageView article1Image = view.findViewById(R.id.imagearticle1);
        TextView titleText = view.findViewById(R.id.article2_title);

        // Handle navigation to ReadingFragment1
        View.OnClickListener navigateToReadingFragment = v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Reading1Activity.class);
            getActivity().startActivity(intent);
        };

        titleText.setOnClickListener(navigateToReadingFragment);
        article1Image.setOnClickListener(navigateToReadingFragment);

        // Initialize account and settings buttons and textviews
        setOnClickListener(view, R.id.accountButton, AccountActivity.class);
        setOnClickListener(view, R.id.accountText, AccountActivity.class);
        setOnClickListener(view, R.id.settingsButton, SettingsActivity.class);
        setOnClickListener(view, R.id.settingsText, SettingsActivity.class);


        ImageButton[] bookmarkButtons = {
                view.findViewById(R.id.bookmark_button),
                view.findViewById(R.id.bookmark_button2),
                view.findViewById(R.id.bookmark_button3),
        };
        for (ImageButton bookmarkButton : bookmarkButtons) {
            bookmarkStates.put(bookmarkButton, true);
            bookmarkButton.setOnClickListener(v -> {
                boolean isBookmarked = bookmarkStates.get(bookmarkButton);
                if (isBookmarked) {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Unbookmarked", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Bookmarked", Toast.LENGTH_SHORT).show();
                    }
                }
                bookmarkStates.put(bookmarkButton, !isBookmarked);
            });
        }
        return view;
    }

    // Helper method to set OnClickListener
    private void setOnClickListener(View view, int viewId, Class<?> targetActivity) {
        view.findViewById(viewId).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        });
    }
}
