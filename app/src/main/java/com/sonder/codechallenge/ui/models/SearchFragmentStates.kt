package com.sonder.codechallenge.ui.models

import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchSectionResult

sealed class SearchFragmentStates {
    data object Started : SearchFragmentStates()
    data class ResultsLoaded(val sections: List<SearchItemRequest>) : SearchFragmentStates()
    data class Error(val message: String) : SearchFragmentStates()
    data object NoResults : SearchFragmentStates()
}