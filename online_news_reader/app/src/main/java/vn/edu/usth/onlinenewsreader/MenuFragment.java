package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MenuFragment extends Fragment {

    private ImageButton homeButton, moreButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Find moreButton in Layout
        moreButton = view.findViewById(R.id.moreButton);
        homeButton = view.findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchHomeFragment();
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMoreFragment();
            }
        });
        return view;
    }

    private void switchHomeFragment() {
        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

        // Make the more_fragment visible
        View fragmentMore = getActivity().findViewById(R.id.fragment_more);
        if (fragmentMore != null) {
            fragmentMore.setVisibility(View.GONE);
        }

        // Make the TabLayout gone
        View tabLayout = getActivity().findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setVisibility(View.VISIBLE);
        }

        // Make the main_screen_fragment gone
        View view_pager = getActivity().findViewById(R.id.view_pager);
        if (view_pager != null) {
            view_pager.setVisibility(View.VISIBLE);
        }


        fragmentTransaction.replace(R.id.fragment_more, mainScreenFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void switchMoreFragment() {
        MoreFragment moreFragment = new MoreFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

        // Make the more_fragment visible
        View fragmentMore = getActivity().findViewById(R.id.fragment_more);
        if (fragmentMore != null) {
            fragmentMore.setVisibility(View.VISIBLE);
        }

        // Make the TabLayout gone
        View tabLayout = getActivity().findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setVisibility(View.GONE);
        }

        // Make the main_screen_fragment gone
        View view_pager = getActivity().findViewById(R.id.view_pager);
        if (view_pager != null) {
            view_pager.setVisibility(View.GONE);
        }


        fragmentTransaction.replace(R.id.fragment_more, moreFragment);

        // Allow to back to previous action
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

