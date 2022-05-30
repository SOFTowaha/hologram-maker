package com.sergioloc.hologram.ui.pyramid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sergioloc.hologram.R
import com.sergioloc.hologram.data.model.Step
import com.sergioloc.hologram.databinding.FragmentPyramidBinding
import com.sergioloc.hologram.ui.adapters.StepsAdapter

/**
 * Created by Sergio López Ceballos on 28/08/2019.
 */

class PyramidFragment : Fragment() {

    private lateinit var binding: FragmentPyramidBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPyramidBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity?
        activity?.title = resources.getString(R.string.title_pyramid)


        // View Pager
        val steps = listOf(
            Step(R.drawable.img_pyramid_0, resources.getString(R.string.step0), resources.getString(R.string.step0Des)),
            Step(R.drawable.img_pyramid_1, resources.getString(R.string.step1), resources.getString(R.string.step1Des)),
            Step(R.drawable.img_pyramid_2, resources.getString(R.string.step2), resources.getString(R.string.step2Des)),
            Step(R.drawable.img_pyramid_3, resources.getString(R.string.step3), resources.getString(R.string.step3Des)),
            Step(R.drawable.img_pyramid_4, resources.getString(R.string.step4), resources.getString(R.string.step4Des)),
            Step(R.drawable.img_pyramid_5, resources.getString(R.string.step5), resources.getString(R.string.step5Des)),
            Step(R.drawable.img_pyramid_6, resources.getString(R.string.step6), resources.getString(R.string.step6Des)),
            Step(R.drawable.img_pyramid_7, resources.getString(R.string.step7), resources.getString(R.string.step7Des)),
            Step(R.drawable.img_pyramid_8, resources.getString(R.string.step8), resources.getString(R.string.step8Des)),
            Step(R.drawable.img_pyramid_9, resources.getString(R.string.step9), resources.getString(R.string.step9Des)),
        )

        binding.vpSteps.adapter = StepsAdapter(requireContext(), steps)
        binding.tabIndicator.setupWithViewPager(binding.vpSteps)
    }

}