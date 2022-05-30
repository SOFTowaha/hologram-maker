package com.sergioloc.hologram.ui.pyramid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sergioloc.hologram.R
import com.sergioloc.hologram.databinding.FragmentPyramidBinding

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
        val activity = activity as AppCompatActivity?
        activity?.title = resources.getString(R.string.title_pyramid)
    }

}