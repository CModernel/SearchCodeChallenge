package com.sonder.domain.repositories

import com.sonder.domain.models.SearchState
import kotlinx.coroutines.flow.Flow

interface SearchResultRepository {
    val searchState : Flow<SearchState>
    fun updateSearchState(state: SearchState)
}