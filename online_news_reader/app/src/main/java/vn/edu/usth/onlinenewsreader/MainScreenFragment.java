package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        // Get references to bookmark buttons (for each article)
        ImageButton bookmarkButton1 = view.findViewById(R.id.bookmark_button);
        ImageButton bookmarkButton2 = view.findViewById(R.id.bookmark_button2);
        ImageButton bookmarkButton3 = view.findViewById(R.id.bookmark_button3);
        ImageButton bookmarkButton4 = view.findViewById(R.id.bookmark_button4);
        ImageButton bookmarkButton5 = view.findViewById(R.id.bookmark_button5);
        ImageButton bookmarkButton6 = view.findViewById(R.id.bookmark_button6);
        ImageButton bookmarkButton7 = view.findViewById(R.id.bookmark_button7);

        // Attach the click listener to bookmark buttons
        bookmarkButton1.setOnClickListener(bookmarkClickListener);
        bookmarkButton2.setOnClickListener(bookmarkClickListener);
        bookmarkButton3.setOnClickListener(bookmarkClickListener);
        bookmarkButton4.setOnClickListener(bookmarkClickListener);
        bookmarkButton5.setOnClickListener(bookmarkClickListener);
        bookmarkButton6.setOnClickListener(bookmarkClickListener);
        bookmarkButton7.setOnClickListener(bookmarkClickListener);


        // Find the TextView or ImageView that represents the article
        TextView articleTitle1 = view.findViewById(R.id.textView1); // assuming textView4 is your article title
        ImageView articleImage1 = view.findViewById(R.id.imagearticle1);
        TextView articleSummary1 = view.findViewById(R.id.textView2);

        // Create an OnClickListener to open the full-screen fragment
        View.OnClickListener fullScreenListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the new full-screen fragment
                ReadingFragment1 readingFragment1 = new ReadingFragment1();

                // Start the fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_reading1, readingFragment1); // 'fragment_container' is the FrameLayout in your activity's layout
                transaction.addToBackStack(null);  // Add to the back stack so user can navigate back
                transaction.commit();
            }
        };

        // Attach the click listener to both the ImageView and TextView
        articleImage1.setOnClickListener(fullScreenListener);
        articleSummary1.setOnClickListener(fullScreenListener);
        articleTitle1.setOnClickListener(fullScreenListener);

        return view;
    }

    // FOR BOOKMARK
    // Create a common OnClickListener
    private final View.OnClickListener bookmarkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton clickedButton = (ImageButton) v;
            toggleBookmarkIcon(clickedButton);
        }
    };

    // Method to toggle bookmark icon state
    private void toggleBookmarkIcon(ImageButton bookmarkButton) {
        if (bookmarkButton.getTag() != null && bookmarkButton.getTag().equals("bookmarked")) {
            // Change to unfilled bookmark (not bookmarked)
            bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
            bookmarkButton.setTag("unbookmarked");
        } else {
            // Change to filled bookmark (bookmarked)
            bookmarkButton.setImageResource(R.drawable.ic_bookmark_filled);
            bookmarkButton.setTag("bookmarked");
        }
    }


}