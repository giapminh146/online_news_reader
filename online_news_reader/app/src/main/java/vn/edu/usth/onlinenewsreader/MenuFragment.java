package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class MenuFragment extends Fragment {

    private ViewPager2 viewPager;
    private LinearLayout homeLayout, moreLayout;
    private ImageButton homeButton, moreButton;
    private TextView homeText, moreText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

//        // Initialize buttons and layouts
//        viewPager = view.findViewById(R.id.viewPager);
//        homeLayout = view.findViewById(R.id.homeLayout);
//        moreLayout = view.findViewById(R.id.moreLayout);
//        homeButton = view.findViewById(R.id.homeButton);
//        moreButton = view.findViewById(R.id.moreButton);
//        homeText = view.findViewById(R.id.homeTextView);
//        moreText = view.findViewById(R.id.moreTextView);
//
//        // Set up ViewPager with an adapter
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
//        viewPager.setAdapter(adapter);
//
//        // Handle button clicks
//        homeLayout.setOnClickListener(v -> setCurrentPage(0));
//        moreLayout.setOnClickListener(v -> setCurrentPage(1));
//        homeButton.setOnClickListener(v -> setCurrentPage(0));
//        moreButton.setOnClickListener(v -> setCurrentPage(1));
//
//        // Page change listener to update button colors
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                updateButtonColors(position);
//            }
//        });

        return view;
    }

//    // Set the current page in the ViewPager
//    private void setCurrentPage(int page) {
//        viewPager.setCurrentItem(page);
//        updateButtonColors(page);
//    }
//
//    // Update the button and text colors based on the selected page
//    private void updateButtonColors(int position) {
//        if (position == 0) {
//            homeButton.setImageResource(R.drawable.ic_home_filled);
//            homeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
//            moreButton.setImageResource(R.drawable.ic_more_border);
//            moreText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
//        } else if (position == 1) {
//            moreButton.setImageResource(R.drawable.ic_more_filled);
//            moreText.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
//            homeButton.setImageResource(R.drawable.ic_home_border);
//            homeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
//        }
//    }

//    // Adapter for ViewPager
//    public class ViewPagerAdapter extends FragmentStateAdapter {
//        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
//            super(fragmentActivity);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int page) {
//            switch (page) {
//                case 0:
//                    return new MainScreenFragment();  // Home screen
//                case 1:
//                    return new MoreFragment();        // More screen
//                default:
//                    return new MainScreenFragment();
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return 2;
//        }
//    }
}
