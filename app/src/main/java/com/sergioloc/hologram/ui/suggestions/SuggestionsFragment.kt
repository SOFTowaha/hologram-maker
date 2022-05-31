package com.sergioloc.hologram.ui.suggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.databinding.FragmentSuggestionsBinding

class SuggestionsFragment: Fragment() {

    private lateinit var binding: FragmentSuggestionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObservers()
        initButtons()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.suggestions)

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

        binding.btnSend.setOnSingleClickListener {

        }
    }

    private fun showLoader(visible: Boolean) {
        if (visible) {
            binding.loader.visible()
            binding.etField.gone()
            binding.tvDescription.gone()
            binding.btnSend.gone()
        }
        else {
            binding.loader.gone()
            binding.etField.visible()
            binding.tvDescription.visible()
            binding.btnSend.visible()
        }
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