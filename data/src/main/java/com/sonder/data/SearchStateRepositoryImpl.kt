package com.sonder.data

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SearchStateRepositoryImpl @Inject constructor() : SearchStateRepository {
    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.Idle)
    override val searchState : Flow<SearchState>
        get() = _searchState

    override fun updateSearchState(state: SearchState) {
        _searchState.value = state
    }
}