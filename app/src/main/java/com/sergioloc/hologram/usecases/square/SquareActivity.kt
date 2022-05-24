package com.sergioloc.hologram.usecases.square

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.databinding.ActivitySquareBinding
import com.sergioloc.hologram.utils.Converter
import com.sergioloc.hologram.utils.Session


class SquareActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySquareBinding
    private var buttonsVisible = true
    private var size = 100
    private var distance = 0
    private var width = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initButtons()

        hideButtonsAfter(1.5f)
        width = Converter.pxToDp(this, getScreenWidth().toFloat())
    }

    private fun initView() {
        Session.bitmap?.let {
            binding.ivTop.setImageBitmap(it)
            binding.ivBottom.setImageBitmap(it)
            binding.ivLeft.setImageBitmap(it)
            binding.ivRight.setImageBitmap(it)
        }
    }

    private fun initButtons() {
        binding.root.setOnClickListener {
            if (buttonsVisible)
                hideButtons()
            else {
                showButtons()
            }
        }

        // Size
        binding.ivLessSize.setOnClickListener {
            if (size > 10) {
                size -= 10
                updateSize()
            }
        }
        binding.ivMoreSize.setOnClickListener {
            if (3 * size + 2 * distance < width) {
                size += 10
                updateSize()
            }
        }

        // Distance
        binding.ivLessDistance.setOnClickListener {
            if (distance > 0) {
                distance -= 10
                updateDistance()
            }
        }
        binding.ivMoreDistance.setOnClickListener {
            if (3 * size + 2 * distance < width) {
                distance += 10
                updateDistance()
            }
        }
    }

    private fun showButtons() {
        buttonsVisible = true
        binding.ivLessSize.visible()
        binding.ivMoreSize.visible()
        binding.tvSize.visible()

        binding.ivLessDistance.visible()
        binding.ivMoreDistance.visible()
        binding.tvDistance.visible()
    }

    private fun hideButtons() {
        buttonsVisible = false
        binding.ivLessSize.gone()
        binding.ivMoreSize.gone()
        binding.tvSize.gone()

        binding.ivLessDistance.gone()
        binding.ivMoreDistance.gone()
        binding.tvDistance.gone()
    }

    private fun hideButtonsAfter(seconds: Float) {
        Handler(Looper.getMainLooper()).postDelayed({
            hideButtons()
        }, (seconds * 1000).toLong())
    }

    private fun updateSize() {
        binding.ivCross.layoutParams.height = Converter.dpToPx(this, size.toFloat())
        binding.ivCross.layoutParams.width = Converter.dpToPx(this, size.toFloat())
        binding.ivCross.requestLayout()
    }

    private fun updateDistance() {
        // Top
        var param = binding.ivTop.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,0, Converter.dpToPx(this, distance.toFloat()))
        binding.ivTop.layoutParams = param

        // Bottom
        param = binding.ivBottom.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, Converter.dpToPx(this, distance.toFloat()),0,0)
        binding.ivBottom.layoutParams = param

        // Left
        param = binding.ivLeft.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0, Converter.dpToPx(this, distance.toFloat()),0)
        binding.ivLeft.layoutParams = param

        // Right
        param = binding.ivRight.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(Converter.dpToPx(this, distance.toFloat()),0,0,0)
        binding.ivRight.layoutParams = param
    }

    private fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

}