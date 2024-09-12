package vn.edu.usth.onlinenewsreader;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.activity.OnBackPressedCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_fragment, container, false);

//        TextView accoutTextView = view.findViewById(R.id.accountText);
//        TextView settingsTextView = view.findViewById(R.id.settingsText);
//        ImageView accountImageView = view.findViewById(R.id.accountButton);
//        ImageView settingsImageView = view.findViewById(R.id.settingsButton);
//
//        // OnClickListener to open full screen fragment
//        View.OnClickListener fullScreenListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SettingsFragment settingsFragment = new SettingsFragment();
//
//                // start fragment transaction
//                FragmentTransaction transaction1 = getParentFragmentManager().beginTransaction();
//                transaction1.replace(R.id.fragment_settings, settingsFragment);
//                transaction1.addToBackStack(null);
//                transaction1.commit();
//
//                getActivity().findViewById(R.id.fragment_settings).setVisibility(View.VISIBLE);
//            }
//        };
//
//        // Attach click listener to ImageView and TextView of Settings
//        settingsTextView.setOnClickListener(fullScreenListener);
//        settingsImageView.setOnClickListener(fullScreenListener);
//
//        // Handle back button press
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // When back is pressed, hide the settings fragment if visible
//                View settingsFragmentView = getActivity().findViewById(R.id.fragment_settings);
//                if (settingsFragmentView.getVisibility() == View.VISIBLE) {
//                    settingsFragmentView.setVisibility(View.GONE);
//                    getParentFragmentManager().popBackStack();  // Remove SettingsFragment from back stack
//                } else {
//                    // If not handling SettingsFragment, disable this callback
//                    setEnabled(false);
//                    requireActivity().getOnBackPressedDispatcher().onBackPressed();  // Forward back press to the system
//                }
//            }
//        });
        return view;
    }
}
