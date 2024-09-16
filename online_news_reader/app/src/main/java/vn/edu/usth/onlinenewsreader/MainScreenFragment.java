package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
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

    // Create a Map to save the bookmark state for each node
    private final Map<ImageButton, Boolean> bookmarkStates = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        // Find id components in the interface
        TextView titleText = view.findViewById(R.id.textView1);
        TextView desciptionText = view.findViewById(R.id.textView2);
        // getActivity() use to get the Activity containing MainScreenFragment. And find the id of TabLayout
        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);

        // Assign event to button
        titleText.setOnClickListener(v -> {
            //making the tablayout bar disappear, still keeping menu and header
            tabLayout.setVisibility(View.GONE);

            // Switch to another screen or handle the event when the button is clicked
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.view_pager_container, new ReadingFragment1());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Assign event to button
        desciptionText.setOnClickListener(v -> {
            //making the tablayout bar disappear, still keeping menu and header
            tabLayout.setVisibility(View.GONE);

            // Switch to another screen or handle the event when the button is clicked
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.view_pager_container, new ReadingFragment1());
            transaction.addToBackStack("home");
            transaction.commit();
        });

        //Create a list, find all bookmarkButoon and add them to the Map
        ImageButton[] bookmarkButtons = {
                view.findViewById(R.id.bookmark_button),
                view.findViewById(R.id.bookmark_button2),
                view.findViewById(R.id.bookmark_button3),
                view.findViewById(R.id.bookmark_button4),
                view.findViewById(R.id.bookmark_button5),
                view.findViewById(R.id.bookmark_button6),
        };

        // Set the status for all bookmark buttons as unbookmarked
        for (ImageButton bookmarkButton : bookmarkButtons) {
            bookmarkStates.put(bookmarkButton, false);

            bookmarkButton.setOnClickListener(v -> {
                // Get the curent state of the button
                boolean isBookmarked = bookmarkStates.get(bookmarkButton);

                // Change icon based on the state
                if (isBookmarked) {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
                } else {
                    bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
                }

                // Update bookmark status
                bookmarkStates.put(bookmarkButton, !isBookmarked);
            });
        }

        
        return view;
    }
}