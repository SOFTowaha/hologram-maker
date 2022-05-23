package com.sergioloc.hologram.usecases.pyramid

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.sergioloc.hologram.R
import com.sergioloc.hologram.usecases.onboarding.OnBoardingFragment

/**
 * Created by Sergio López Ceballos on 28/08/2019.
 */

class PyramidFragment : Fragment() {

    var adapterViewPager: FragmentPagerAdapter? = null
    private var viewFragment: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_how, container, false)
        val vpPager = viewFragment?.findViewById(R.id.pager) as ViewPager
        adapterViewPager = MyPagerAdapter(requireFragmentManager())
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
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 // Fragment # 0
                -> OnBoardingFragment(0)
                1 // Fragment # 1
                -> OnBoardingFragment(1)
                2 // Fragment # 2
                -> OnBoardingFragment(2)
                3 // Fragment # 3
                -> OnBoardingFragment(3)
                4 // Fragment # 4
                -> OnBoardingFragment(4)
                5 // Fragment # 5
                -> OnBoardingFragment(5)
                else -> Fragment()
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