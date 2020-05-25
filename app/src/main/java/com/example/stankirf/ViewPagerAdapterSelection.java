package com.example.stankirf;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapterSelection extends FragmentStateAdapter {

    public ViewPagerAdapterSelection(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 :
                return new Fragment1();
            case 1 :
                return new FavoriteFragment();
            default:
                return new FavoriteFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
