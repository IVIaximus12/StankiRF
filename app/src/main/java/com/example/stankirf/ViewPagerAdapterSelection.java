package com.example.stankirf;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapterSelection extends FragmentStateAdapter {

    private String idMachine;

    public ViewPagerAdapterSelection(@NonNull FragmentActivity fragmentActivity, String idMachine) {
        super(fragmentActivity);
        this.idMachine = idMachine;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 :
                return new DetailedFragment(idMachine);
            case 1 :
                return new FavoriteFragment();
            default:
                return new DetailedFragment(idMachine);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
