package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.SuggestionRepository
import com.sergioloc.hologram.domain.model.Suggestion
import com.sergioloc.hologram.utils.Constants
import javax.inject.Inject

class SaveSuggestionUseCase @Inject constructor(
    private val repository: SuggestionRepository
) {

    private val prefix = arrayListOf(
        "https://www.youtube.",
        "http://www.youtube",
        "www.youtube.",
        "youtube.",
        "www.youtube.",
        "https://www.youtu.be/",
        "http://www.youtu.be/",
        "https://youtu.be/",
        "http://youtu.be/",
    )

    suspend operator fun invoke(suggestion: Suggestion): Int {
        return if (suggestion.text.isEmpty())
            Constants.EMPTY_FIELD
        else if (suggestion.type == 1 && !isYouTubeURL(suggestion.text))
            Constants.FORMAT_ERROR
        else
            repository.putSuggestion(suggestion)
    }

    private fun isYouTubeURL(url: String): Boolean {
        for (p in prefix)
            if (url.startsWith(p))
                return true
        return false
    }

}