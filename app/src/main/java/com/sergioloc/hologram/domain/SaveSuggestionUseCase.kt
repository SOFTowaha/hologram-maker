package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.AppRepository
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.data.model.Suggestion

class SaveSuggestionUseCase {

    private val repository = AppRepository()

    suspend operator fun invoke(suggestion: Suggestion): Boolean = repository.putSuggestion(suggestion)

}