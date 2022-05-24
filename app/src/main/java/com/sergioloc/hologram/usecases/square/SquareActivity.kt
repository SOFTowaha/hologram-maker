package com.sergioloc.hologram.usecases.square

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.databinding.ActivitySquareBinding
import com.sergioloc.hologram.utils.Session

class SquareActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySquareBinding
    private var buttonsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initButtons()

        hideButtonsAfter(1.5f)
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

        }
        binding.ivMoreSize.setOnClickListener {

        }

        // Distance
        binding.ivLessDistance.setOnClickListener {

        }
        binding.ivMoreDistance.setOnClickListener {

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

}