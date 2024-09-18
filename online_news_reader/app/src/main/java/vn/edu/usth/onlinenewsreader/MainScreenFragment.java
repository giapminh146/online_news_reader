package vn.edu.usth.onlinenewsreader;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import java.util.HashMap;
import java.util.Map;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreenFragment extends Fragment {

    private final Map<ImageButton, Boolean> bookmarkStates = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        ImageView article1Image = view.findViewById(R.id.imagearticle1);
        TextView titleText = view.findViewById(R.id.textView1);
        TextView descriptionText = view.findViewById(R.id.textView2);
        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);

        // Handle navigation to ReadingFragment1
        View.OnClickListener navigateToReadingFragment = v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Reading1Activity.class);
            getActivity().startActivity(intent);
        };

        titleText.setOnClickListener(navigateToReadingFragment);
        descriptionText.setOnClickListener(navigateToReadingFragment);
        article1Image.setOnClickListener(navigateToReadingFragment);

        ImageButton[] bookmarkButtons = {
                view.findViewById(R.id.bookmark_button),
                view.findViewById(R.id.bookmark_button2),
                view.findViewById(R.id.bookmark_button3),
                view.findViewById(R.id.bookmark_button4),
                view.findViewById(R.id.bookmark_button5),
                view.findViewById(R.id.bookmark_button6),
                view.findViewById(R.id.bookmark_button7),
        };

        for (ImageButton bookmarkButton : bookmarkButtons) {
            bookmarkStates.put(bookmarkButton, false);
            bookmarkButton.setOnClickListener(v -> {
                boolean isBookmarked = bookmarkStates.get(bookmarkButton);
                if (isBookmarked) {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
                } else {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
                }
                bookmarkStates.put(bookmarkButton, !isBookmarked);
            });
        }
        return view;
    }
}
