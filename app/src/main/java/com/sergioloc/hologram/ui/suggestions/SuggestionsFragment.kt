package com.sergioloc.hologram.ui.suggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.databinding.FragmentSuggestionsBinding
import com.sergioloc.hologram.domain.model.Suggestion
import com.sergioloc.hologram.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestionsFragment: Fragment() {

    private lateinit var binding: FragmentSuggestionsBinding
    private val viewModel: SuggestionsViewModel by viewModels()
    private var commentSelected = false

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

        // Ad
        binding.adBanner.loadAd(AdRequest.Builder().build())
    }

    private fun initObservers() {
        viewModel.response.observe(viewLifecycleOwner) {
            it.onSuccess { code ->
                showLoader(false)
                when (code) {
                    Constants.SUCCESS -> {
                        binding.etField.text.clear()
                        Toast.makeText(requireContext(), getString(R.string.suggestion_sent), Toast.LENGTH_SHORT).show()
                    }
                    Constants.ERROR -> {
                        Toast.makeText(requireContext(), getString(R.string.error_suggestion), Toast.LENGTH_SHORT).show()
                    }
                    Constants.EMPTY_FIELD -> {
                        Toast.makeText(requireContext(), getString(R.string.type_message), Toast.LENGTH_SHORT).show()
                    }
                    Constants.FORMAT_ERROR -> {
                        Toast.makeText(requireContext(), getString(R.string.invalid_youtube_url), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initButtons() {
        binding.tvYoutube.setOnClickListener {
            showYoutube()
        }

        binding.tvComment.setOnClickListener {
            showComment()
        }

        binding.btnSend.setOnSingleClickListener {
            showLoader(true)
            var type = 1
            if (commentSelected)
                type = 2
            viewModel.saveSuggestion(Suggestion(type, binding.etField.text.toString()))
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
        commentSelected = false
        binding.tvYoutube.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        binding.tvComment.setTextColor(ContextCompat.getColor(requireContext(), R.color.textDefault))
        binding.tvYoutube.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_stroke_primary_dark)
        binding.tvComment.background = null
        binding.etField.hint = getString(R.string.youtube_link)
        binding.tvDescription.text = getString(R.string.youtube_link_des)
    }

    private fun showComment() {
        commentSelected = true
        binding.tvYoutube.setTextColor(ContextCompat.getColor(requireContext(), R.color.textDefault))
        binding.tvComment.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        binding.tvYoutube.background = null
        binding.tvComment.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_stroke_primary_dark)
        binding.etField.hint = getString(R.string.comment)
        binding.tvDescription.text = getString(R.string.comment_des)
    }

}