package com.sergioloc.hologram.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.sergioloc.hologram.Fragments.Step0Fragment;
import com.sergioloc.hologram.Fragments.Step1Fragment;
import com.sergioloc.hologram.Fragments.Step2Fragment;
import com.sergioloc.hologram.Fragments.Step3Fragment;
import com.sergioloc.hologram.Fragments.Step4Fragment;
import com.sergioloc.hologram.Fragments.Step5Fragment;
import com.sergioloc.hologram.R;

public class SlidePageActivity extends AppCompatActivity {

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
                    return Step0Fragment.newInstance();
                case 1: // Fragment # 1
                    return Step1Fragment.newInstance();
                case 2: // Fragment # 2
                    return Step2Fragment.newInstance();
                case 3: // Fragment # 3
                    return Step3Fragment.newInstance();
                case 4: // Fragment # 4
                    return Step4Fragment.newInstance();
                case 5: // Fragment # 5
                    return Step5Fragment.newInstance();
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
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_how);
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


        /**Back Button**/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /**Back Button**/
    public void onBackPressed(){
        startActivity(new Intent(SlidePageActivity.this, NavigationActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            startActivity(new Intent(SlidePageActivity.this, NavigationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}