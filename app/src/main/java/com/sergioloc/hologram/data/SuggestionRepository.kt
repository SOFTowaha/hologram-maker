package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.domain.model.Suggestion
import com.sergioloc.hologram.domain.model.toData
import javax.inject.Inject

class SuggestionRepository @Inject constructor(
    private val api: FirebaseService
) {

    suspend fun putSuggestion(suggestion: Suggestion): Int {
        return api.putSuggestion(suggestion.toData())
    }

}