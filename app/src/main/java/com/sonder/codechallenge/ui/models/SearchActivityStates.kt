package com.sonder.codechallenge.ui.models

import com.sonder.domain.models.SearchSectionResult

sealed class SearchActivityStates {
    object Started : SearchActivityStates()
    data class Searching(val query: String) : SearchActivityStates()
    data class Results(val query: String) : SearchActivityStates()
    data class Error(val message: String) : SearchActivityStates()
}