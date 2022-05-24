package com.sergioloc.hologram.usecases.square

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergioloc.hologram.databinding.ActivitySquareBinding
import com.sergioloc.hologram.utils.Session


class SquareActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySquareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Session.bitmap?.let {
            binding.ivTop.setImageBitmap(it)
            binding.ivBottom.setImageBitmap(it)
            binding.ivLeft.setImageBitmap(it)
            binding.ivRight.setImageBitmap(it)
        }
    }

}