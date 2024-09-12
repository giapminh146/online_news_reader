package vn.edu.usth.onlinenewsreader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NewsPagerAdapter extends FragmentPagerAdapter {

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @NonNull
    @Override
    public Fragment getItem(int page) {
        switch (page) {
            case 0: return new MainScreenFragment();
            case 1: return new SportFragment();
            case 2:
            case 3:
            case 4:
                return new EmptyFragment();
        }
        return new EmptyFragment();
    }
    @Override
    public CharSequence getPageTitle(int page) {
        final String[] titles = new String[] { "All", "Sport", "Business", "Entertainment", "Health" };
        return titles[page];
    }
}
