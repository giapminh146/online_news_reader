package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

public class SportFragment extends Fragment {
    private final Map<ImageButton, Boolean> bookmarkStates = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport, container, false);
        //Create a list, find all bookmarkButoon and add them to the Map
        ImageButton[] bookmarkButtons = {
                view.findViewById(R.id.bookmark_button),
                view.findViewById(R.id.bookmark_button2),
                view.findViewById(R.id.bookmark_button3),
                view.findViewById(R.id.bookmark_button4),
                view.findViewById(R.id.bookmark_button6),
                view.findViewById(R.id.bookmark_button7),
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