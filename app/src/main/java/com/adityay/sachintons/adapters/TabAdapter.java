package com.adityay.sachintons.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adityay.sachintons.fragments.MainFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    public HashMap<Integer, Fragment> hashMap = new HashMap<>();

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }




    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = mFragmentList.get(position);
        hashMap.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}
