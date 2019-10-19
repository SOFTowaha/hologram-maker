package com.sergioloc.hologram.Views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.sergioloc.hologram.R

/**
 * Created by Sergio López Ceballos on 28/08/2019.
 */

class PyramidFragment : Fragment() {

    var adapterViewPager: FragmentPagerAdapter? = null
    private var viewFragment: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_how, container, false)
        val vpPager = viewFragment?.findViewById(R.id.pager) as ViewPager
        adapterViewPager = MyPagerAdapter(fragmentManager!!)
        vpPager.adapter = adapterViewPager
        val activity = activity as AppCompatActivity?
        activity?.title = resources.getString(R.string.title_pyramid)
        return viewFragment
    }

    /** Internal class  */

    class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        // Returns total number of pages
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 // Fragment # 0
                -> return StepFragment(0)
                1 // Fragment # 1
                -> return StepFragment(1)
                2 // Fragment # 2
                -> return StepFragment(2)
                3 // Fragment # 3
                -> return StepFragment(3)
                4 // Fragment # 4
                -> return StepFragment(4)
                5 // Fragment # 5
                -> return StepFragment(5)
                else -> return null
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }

        companion object {
            private val NUM_ITEMS = 6
        }

    }
}