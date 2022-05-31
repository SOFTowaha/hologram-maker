package com.sergioloc.hologram.ui.suggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sergioloc.hologram.R
import com.sergioloc.hologram.databinding.FragmentSuggestBinding


class SuggestionsFragment: Fragment() {

    private lateinit var binding: FragmentSuggestBinding
    val courses = arrayOf("YouTube video", "Comment")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSuggestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initVariables()
        initListeners()
        initObservers()
        initButtons()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.suggestions)

    }

    private fun initVariables() {

    }

    private fun initListeners() {


    }

    private fun initObservers() {

    }

    private fun initButtons() {
        binding.tvYoutube.setOnClickListener {
            showYoutube()
        }
        binding.tvComment.setOnClickListener {
            showComment()
        }
    }

    private fun showLoader(visible: Boolean) {

    }
    
    private fun showYoutube() {
        binding.tvYoutube.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        binding.tvComment.setTextColor(ContextCompat.getColor(requireContext(), R.color.textDefault))
        binding.tvYoutube.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_stroke_primary_dark)
        binding.tvComment.background = null
        binding.etField.hint = getString(R.string.youtube_link)
        binding.tvDescription.text = getString(R.string.youtube_link_des)
    }

    private fun showComment() {
        binding.tvYoutube.setTextColor(ContextCompat.getColor(requireContext(), R.color.textDefault))
        binding.tvComment.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        binding.tvYoutube.background = null
        binding.tvComment.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_stroke_primary_dark)
        binding.etField.hint = getString(R.string.comment)
        binding.tvDescription.text = getString(R.string.comment_des)
    }

}