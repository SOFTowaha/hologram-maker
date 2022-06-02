package com.sergioloc.hologram.ui.square

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.databinding.ActivitySquareBinding
import com.sergioloc.hologram.utils.Converter
import com.sergioloc.hologram.utils.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SquareActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySquareBinding
    private var width = 0f
    private var size = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVariables()
        initView()
        initListeners()
        initButtons()

        updateSize()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.seekBarSize.gone()
        }, (1.5 * 1000).toLong())
    }

    private fun initVariables() {
        width = Converter.pxToDp(this, getScreenWidth().toFloat())
        size = (width / 3).toInt()
    }

    private fun initView() {
        Session.bitmap?.let {
            binding.ivTop.setImageBitmap(it)
            binding.ivBottom.setImageBitmap(it)
            binding.ivLeft.setImageBitmap(it)
            binding.ivRight.setImageBitmap(it)
        }

        binding.seekBarSize.max = size
        binding.seekBarSize.progress = size
        binding.seekBarSize.incrementProgressBy(10)
    }

    private fun initListeners() {
        binding.seekBarSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                size = if (progress < 10)
                    10
                else
                    progress
                updateSize()
            }

            override fun onStartTrackingTouch(seek: SeekBar) { }

            override fun onStopTrackingTouch(seek: SeekBar) { }
        })
    }

    private fun initButtons() {
        binding.root.setOnClickListener {
            if (binding.seekBarSize.visibility == View.VISIBLE)
                binding.seekBarSize.gone()
            else {
                binding.seekBarSize.visible()
            }
        }
    }

    private fun updateSize() {
        binding.ivCross.layoutParams.height = Converter.dpToPx(this, size.toFloat())
        binding.ivCross.layoutParams.width = Converter.dpToPx(this, size.toFloat())
        binding.ivCross.requestLayout()
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