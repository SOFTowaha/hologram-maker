package com.sergioloc.hologram.ui.suggestions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.data.model.Suggestion
import com.sergioloc.hologram.domain.SaveSuggestionUseCase
import kotlinx.coroutines.launch

class SuggestionsViewModel: ViewModel() {

    val response: MutableLiveData<Result<Int>> = MutableLiveData()
    private val saveSuggestionUseCase = SaveSuggestionUseCase()

    fun saveSuggestion(suggestion: Suggestion) {
        viewModelScope.launch {
            val result = saveSuggestionUseCase(suggestion)
            response.postValue(Result.success(result))
        }
    }

}