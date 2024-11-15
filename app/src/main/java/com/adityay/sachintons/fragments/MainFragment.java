package com.adityay.sachintons.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adityay.sachintons.R;
import com.adityay.sachintons.adapters.TabAdapter;
import com.adityay.sachintons.databinding.FragmentMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainFragment extends Fragment {

//    @BindView(R.id.tab_layout)
//    TabLayout tabLayout;
//    @BindView(R.id.view_pager)
//    ViewPager viewPager;

    private FragmentMainBinding binding;
    private TabAdapter tabAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_main, container, false);

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //ButterKnife.bind(this, view);
        setupTabAdapter();
        return view;
    }

    private void setupTabAdapter() {
        tabAdapter = new TabAdapter(getChildFragmentManager(), getLifecycle());
        tabAdapter.addFragment(new CenturyFragment(), "Centuries");
        tabAdapter.addFragment(new PostsFragment(), "Posts");
        tabAdapter.addFragment(new AboutFragment(), "About");

        binding.viewPager.setAdapter(tabAdapter);
        binding.viewPager.setOffscreenPageLimit(tabAdapter.getItemCount());

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(tabAdapter.getPageTitle(position))).attach();
    }


    public void reloadFragment() {
        if(binding.viewPager.getAdapter() != null) {
            Fragment fragment = (Fragment) tabAdapter.hashMap.get(binding.viewPager.getCurrentItem());
            fragment.onResume();
        }
    }



    public ViewPager2 getViewPager() {
        return binding.viewPager;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
