package com.sergioloc.hologram.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sergioloc.hologram.R;


public class HowFragment extends Fragment {

    FragmentPagerAdapter adapterViewPager;

    public HowFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how, container, false);

        ViewPager vpPager = (ViewPager) view.findViewById(R.id.pager);
        adapterViewPager = new HowFragment.MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        return view;
    }



    /** Internal class **/

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 6;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0
                    return StepFragment.newInstance(0);
                case 1: // Fragment # 1
                    return StepFragment.newInstance(1);
                case 2: // Fragment # 2
                    return StepFragment.newInstance(2);
                case 3: // Fragment # 3
                    return StepFragment.newInstance(3);
                case 4: // Fragment # 4
                    return StepFragment.newInstance(4);
                case 5: // Fragment # 5
                    return StepFragment.newInstance(5);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
