package com.sergioloc.hologram.domain.model

import com.sergioloc.hologram.data.model.SuggestionModel

data class Suggestion (
    val type: Int,
    val text: String)

fun Suggestion.toData() = SuggestionModel(type, text)