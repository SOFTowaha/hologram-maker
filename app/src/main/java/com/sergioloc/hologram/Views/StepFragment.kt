package com.sergioloc.hologram.Views

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sergioloc.hologram.R

/**
 * Created by Sergio López Ceballos on 28/08/2019.
 */

@SuppressLint("ValidFragment")
class StepFragment(var step: Int): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (step) {
            0 -> inflater?.inflate(R.layout.fragment_step0, container, false)
            1 -> inflater?.inflate(R.layout.fragment_step1, container, false)
            2 -> inflater?.inflate(R.layout.fragment_step2, container, false)
            3 -> inflater?.inflate(R.layout.fragment_step3, container, false)
            4 -> inflater?.inflate(R.layout.fragment_step4, container, false)
            else -> inflater?.inflate(R.layout.fragment_step5, container, false)
        }
    }
}